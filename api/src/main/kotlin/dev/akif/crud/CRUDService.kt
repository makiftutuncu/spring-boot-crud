package dev.akif.crud

import dev.akif.crud.common.InstantProvider
import dev.akif.crud.common.Paged
import dev.akif.crud.common.Parameters
import dev.akif.crud.error.CRUDErrorException.Companion.alreadyExists
import dev.akif.crud.error.CRUDErrorException.Companion.notFound
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.NestedExceptionUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.io.Serializable
import java.sql.SQLException

/**
 * Base implementation of a CRUD service for business layer
 *
 * This is meant to be extended from a **@Service** class.
 *
 * @param I          Id type of the data
 * @param M          Model type of the data which is a [CRUDModel]
 * @param E          Entity type of the data which is a [CRUDEntity]
 * @param CM         Create model type of the data which is a [CRUDCreateModel]
 * @param UM         Update model type of the data which is a [CRUDUpdateModel]
 * @param R          Repository type of the data which is a [CRUDRepository]
 * @param Mapper     Mapper type of the data which is a [CRUDMapper]
 *
 * @property typeName        Type name of the data this service manages
 * @property instantProvider [InstantProvider] dependency of this service
 * @property repository      Repository dependency of this service which is a [CRUDRepository]
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
    open val typeName: String,
    protected open val instantProvider: InstantProvider,
    protected open val repository: R,
    protected open val mapper: Mapper
) {
    /**
     * Creates given entity using [repository] with given parameters
     *
     * @param entity Entity to create
     * @param parameters [Parameters] to use for creating
     * @return Created entity
     */
    protected abstract fun createUsingRepository(entity: E, parameters: Parameters): E

    /**
     * Lists entities using [repository] with given pageable and parameters
     *
     * @param pageable [Pageable] to use for pagination
     * @param parameters [Parameters] to use for listing
     * @return Page of entities
     */
    protected abstract fun listUsingRepository(pageable: Pageable, parameters: Parameters): Page<E>

    /**
     * Gets entity with given id and parameters using [repository]
     *
     * @param id Id of the entity to get
     * @param parameters [Parameters] to use for getting
     * @return Entity with given id
     */
    protected abstract fun getUsingRepository(id: I, parameters: Parameters): E?

    /**
     * Updates given entity using [repository] with given parameters
     *
     * @param entity Entity to update
     * @param parameters [Parameters] to use for updating
     * @return Number of affected entities
     */
    protected abstract fun updateUsingRepository(entity: E, parameters: Parameters): Int

    /**
     * Default implementation for creating a new entity from given create model
     *
     * @param createModel Create model containing data of the entity to create
     * @param parameters [Parameters] to use for creating
     * @return Model of the created entity
     */
    @Transactional(rollbackFor = [Exception::class], isolation = Isolation.READ_COMMITTED)
    open fun create(createModel: CM, parameters: Parameters): M {
        log.info("Creating new $typeName with parameters $parameters: $createModel")
        val entity = mapper.entityToBeCreatedFrom(createModel, instantProvider.now())
        log.trace("Built ${typeName}Entity: $entity")
        val saved = persist(logData = createModel) { createUsingRepository(entity, parameters) }
        log.trace("Saved ${typeName}Entity: $saved")
        val model = mapper.entityToModel(saved)
        log.trace("Built $typeName: $model")
        return model
    }

    /**
     * Default implementation for listing entities with given pagination
     *
     * @param pageable Pageable
     * @param parameters [Parameters] to use for listing
     * @return Page of models of entities
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ, readOnly = true)
    open fun list(pageable: Pageable, parameters: Parameters): Paged<M> {
        log.info("Getting $typeName $pageable with parameters $parameters")
        val entities = listUsingRepository(pageable, parameters)
        log.trace("Found ${typeName}Entity page: ${entities.content}")
        val models = entities.map(mapper::entityToModel)
        log.trace("Built $typeName page: ${models.content}")
        return Paged(models)
    }

    /**
     * Default implementation for getting an entity with given id
     *
     * @param id Id of the entity
     * @param parameters [Parameters] to use for getting
     * @return Model of the entity with given id
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ, readOnly = true)
    open fun get(id: I, parameters: Parameters): M? {
        log.info("Getting $typeName $id with parameters $parameters")
        val entity = getUsingRepository(id, parameters)?.also {
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
     * @param parameters [Parameters] to use for updating
     * @return Model of the updated entity
     */
    @Transactional(rollbackFor = [Exception::class], isolation = Isolation.READ_COMMITTED)
    open fun update(id: I, updateModel: UM, parameters: Parameters): M {
        log.info("Updating $typeName $id with parameters $parameters: $updateModel")
        val entity = getUsingRepository(id, parameters) ?: throw notFound(typeName, id)
        log.trace("Found ${typeName}Entity $id to update: $entity")
        val expectedVersion = entity.version ?: 0
        entity.apply {
            mapper.updateEntityWith(this, updateModel)
            updatedAt = instantProvider.now()
        }
        log.trace("Built ${typeName}Entity $id to update: $entity")
        persist(logData = updateModel) {
            val affected = updateUsingRepository(entity, parameters)
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
     * @param parameters [Parameters] to use for deleting
     */
    @Transactional(rollbackFor = [Exception::class], isolation = Isolation.READ_COMMITTED)
    open fun delete(id: I, parameters: Parameters) {
        log.info("Deleting $typeName $id with parameters $parameters")
        val entity = getUsingRepository(id, parameters) ?: throw notFound(typeName, id)
        log.trace("Found ${typeName}Entity $id to delete: $entity")
        val expectedVersion = entity.version ?: 0
        entity.apply {
            val now = instantProvider.now()
            updatedAt = now
            deletedAt = now
        }
        log.trace("Built ${typeName}Entity $id to delete: $entity")
        persist(logData = "with id $id") {
            val affected = updateUsingRepository(entity, parameters)
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
        @JvmField
        protected val log: Logger = LoggerFactory.getLogger(CRUDService::class.java)
    }
}
