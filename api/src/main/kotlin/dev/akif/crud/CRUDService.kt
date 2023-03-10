package dev.akif.crud

import dev.akif.crud.common.InstantProvider
import dev.akif.crud.common.Paged
import dev.akif.crud.error.CRUDErrorException.Companion.alreadyExists
import dev.akif.crud.error.CRUDErrorException.Companion.notFound
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.NestedExceptionUtils
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import java.io.Serializable
import java.sql.SQLException

/**
 * Base implementation of a CRUD service for business layer
 *
 * This is meant to be extended from a **@Service** class.
 *
 * @param I              Id type of the data
 * @param M              Model type of the data which is a [CRUDModel]
 * @param E              Entity type of the data which is a [CRUDEntity]
 * @param CM             Create model type of the data which is a [CRUDCreateModel]
 * @param UM             Update model type of the data which is a [CRUDUpdateModel]
 * @param R              Repository type of the data which is a [CRUDRepository]
 * @param Mapper         Mapper type of the data which is a [CRUDMapper]
 * @param crudRepository Repository dependency of this service which is a [CRUDRepository] as a base type,
 * private by choice because there is [repository] to access it with the more specific type instead of the base type
 *
 * @property typeName        Type name of the data this service manages
 * @property instantProvider [InstantProvider] dependency of this service
 * @property mapper          Mapper dependency of this service which is a [CRUDMapper]
 */
abstract class CRUDService<
        I : Serializable,
        E : CRUDEntity<I>,
        out M : CRUDModel<I>,
        in CM : CRUDCreateModel,
        in UM : CRUDUpdateModel,
        out R : CRUDRepository<I, E>,
        out Mapper : CRUDMapper<I, E, M, CM, UM>>(
    protected open val typeName: String,
    protected open val instantProvider: InstantProvider,
    crudRepository: CRUDRepository<I, E>,
    protected open val mapper: Mapper
) {
    /**
     * Repository dependency of this service as a more specific type
     */
    @Suppress("UNCHECKED_CAST")
    protected val repository: R = crudRepository as R

    /**
     * Default implementation for creating a new entity from given create model
     *
     * @param createModel Create model containing data of the entity to create
     * @return Model of the created entity
     */
    @Transactional
    open fun create(createModel: CM): M {
        log.info("Creating new $typeName: $createModel")
        val entity = mapper.entityToBeCreatedFrom(createModel, instantProvider.now())
        log.trace("Built ${typeName}Entity: $entity")
        val saved = persist(logData = createModel) {
            repository.save(entity)
        }
        log.trace("Saved ${typeName}Entity: $saved")
        val model = mapper.entityToModel(saved)
        log.trace("Built $typeName: $model")
        return model
    }

    /**
     * Default implementation for listing entities with given pagination
     *
     * @param pageable Pageable
     * @return Page of models of entities
     */
    open fun getAll(pageable: Pageable): Paged<M> {
        log.info("Getting $typeName $pageable")
        val entities = repository.findAllByDeletedAtIsNull(pageable)
        log.trace("Found ${typeName}Entity $pageable: ${entities.content}")
        val models = entities.map(mapper::entityToModel)
        log.trace("Built $typeName $pageable: ${models.content}")
        return Paged(models)
    }

    /**
     * Default implementation for getting an entity with given id
     *
     * @param id Id of the entity
     * @return Model of the entity with given id
     */
    open fun get(id: I): M? {
        log.info("Getting $typeName $id")
        val entity = repository.findByIdAndDeletedAtIsNull(id)?.also {
            log.trace("Found ${typeName}Entity $id: $it")
        }
        return entity?.let {
            val m = mapper.entityToModel(it)
            log.trace("Built $typeName $id: $m")
            m
        }
    }

    /**
     * Default implementation for updating an entity with given id with given update model data
     *
     * @param id          Id of the entity to update
     * @param updateModel Update model containing data to be updated
     * @return Model of the updated entity
     */
    @Transactional
    open fun update(id: I, updateModel: UM): M {
        log.info("Updating $typeName $id: $updateModel")
        val entity = repository.findByIdAndDeletedAtIsNull(id) ?: throw notFound(typeName, id)
        log.trace("Found ${typeName}Entity $id to update: $entity")
        val expectedVersion = entity.version ?: 0
        entity.apply {
            mapper.updateEntityWith(this, updateModel)
            updatedAt = instantProvider.now()
        }
        log.trace("Built ${typeName}Entity $id to update: $entity")
        persist(logData = updateModel) {
            val affected = repository.update(entity)
            assertSingleRowIsAffected(affected, expectedVersion)
        }
        entity.version = entity.version?.plus(1)
        log.trace("Updated ${typeName}Entity $id: $entity")
        val model = mapper.entityToModel(entity)
        log.trace("Built $typeName $id: $model")
        return model
    }

    /**
     * Default implementation for deleting an entity with given id
     *
     * @param id Id of the entity to delete
     */
    @Transactional
    open fun delete(id: I) {
        log.info("Deleting $typeName $id")
        val entity = repository.findByIdAndDeletedAtIsNull(id) ?: throw notFound(typeName, id)
        log.trace("Found ${typeName}Entity $id to delete: $entity")
        val expectedVersion = entity.version ?: 0
        entity.apply {
            val now = instantProvider.now()
            updatedAt = now
            deletedAt = now
        }
        log.trace("Built ${typeName}Entity $id to delete: $entity")
        persist(logData = "with id $id") {
            val affected = repository.update(entity)
            assertSingleRowIsAffected(affected, expectedVersion)
        }
        log.trace("Deleted ${typeName}Entity $id")
    }

    /**
     * Performs given persisting action
     *
     * This also flushes the changes made to the entity and performs a duplicate check.
     *
     * @param A       Type of result persisting action returns
     * @param logData Data describing the entity, used to build an error if there is a duplicate
     * @param action  Persisting action to perform
     * @return Result of the persisting action
     */
    protected open fun <A> persist(logData: Any, action: () -> A): A {
        try {
            val result = action()
            repository.flush()
            return result
        } catch (t: Throwable) {
            val alreadyExistsError = NestedExceptionUtils.getMostSpecificCause(t).let {
                it is SQLException && (
                        it.message?.contains("duplicate") == true || it.message?.contains("constraint") == true
                )
            }
            if (alreadyExistsError) {
                throw alreadyExists(typeName, logData)
            }
            throw t
        }
    }

    private fun assertSingleRowIsAffected(affected: Int, expected: Int): Int {
        check(affected == 1) { "Cannot update ${typeName}Entity, entity version wasn't $expected!" }
        return affected
    }

    /** @suppress */
    companion object {
        @JvmStatic
        protected val log: Logger = LoggerFactory.getLogger(CRUDService::class.java)
    }
}
