package dev.akif.crud

import dev.akif.crud.error.CRUDErrorException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.NestedExceptionUtils
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import java.io.Serializable
import java.sql.SQLException
import java.time.Clock
import java.time.Instant
import java.util.function.Supplier

/**
 * Base implementation of a CRUD service for business layer
 *
 * This is meant to be extended from a **@Service** class.
 *
 * @param I      Id type of the data
 * @param M      Model type of the data which is a [CRUDModel]
 * @param E      Entity type of the data which is a [CRUDEntity]
 * @param CM     Create model type of the data which is a [CRUDCreateModel]
 * @param UM     Update model type of the data which is a [CRUDUpdateModel]
 * @param Mapper Mapper type of the data which is a [CRUDMapper]
 *
 * @property typeName   Type name of the data this service manages
 * @property clock      [Clock] dependency of this service
 * @property repository Repository dependency of this service which is a [CRUDRepository]
 * @property mapper     Mapper dependency of this service which is a [CRUDMapper]
 */
abstract class CRUDService<
        I : Serializable,
        out M : CRUDModel<I>,
        E : CRUDEntity<I, E>,
        in CM : CRUDCreateModel,
        in UM : CRUDUpdateModel,
        out Mapper : CRUDMapper<I, E, M, CM, UM>>(
    protected open val typeName: String,
    protected open val clock: Clock,
    protected open val repository: CRUDRepository<I, E>,
    protected open val mapper: Mapper
) {
    /**
     * Default implementation for creating a new entity from given create model
     *
     * @param createModel Create model containing data of the entity to create
     * @return Model of the created entity
     */
    @Transactional
    open fun create(createModel: CM): M {
        log.info("Creating new {}: {}", typeName, createModel)
        val entity = mapper.entityToBeCreatedFrom(createModel, Instant.now(clock))
        log.trace("Built {}Entity: {}", typeName, entity)
        val saved = persist({ repository.save(entity) }, createModel)
        log.trace("Saved {}Entity: {}", typeName, saved)
        val model = mapper.entityToModel(saved)
        log.trace("Built {}: {}", typeName, model)
        return model
    }

    /**
     * Default implementation for listing entities with given pagination
     *
     * @param pageable Pageable
     * @return Page of models of entities
     */
    open fun getAll(pageable: Pageable): Paged<M> {
        log.info("Getting {} {}", typeName, pageable)
        val entities = repository.findAllByDeletedAtIsNull(pageable)
        log.trace("Found {}Entity {}: {}", typeName, pageable, entities.content)
        val models = entities.map(mapper::entityToModel)
        log.trace("Built {} {}: {}", typeName, pageable, models.content)
        return Paged(models)
    }

    /**
     * Default implementation for getting an entity with given id
     *
     * @param id Id of the entity
     * @return Model of the entity with given id
     */
    open fun get(id: I): M? {
        log.info("Getting {} {}", typeName, id)
        val entity = repository.findByIdAndDeletedAtIsNull(id)?.also {
            log.trace("Found {}Entity {}: {}", typeName, id, it)
        }
        return entity?.let {
            val m = mapper.entityToModel(it)
            log.trace("Built {} {}: {}", typeName, id, m)
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
        log.info("Updating {} {}: {}", typeName, id, updateModel)
        val entity =
            repository.findByIdAndDeletedAtIsNull(id) ?: throw CRUDErrorException.notFound(typeName, id)
        log.trace("Found {}Entity {} to update: {}", typeName, id, entity)
        val updated = mapper.updateEntityWith(updateModel, entity).updatedNow(Instant.now(clock))
        log.trace("Built {}Entity {} to update: {}", typeName, id, updated)
        val expectedVersion = updated.version ?: 0
        persist(
            { assertSingleRowIsAffected(repository.updateByVersion(updated, expectedVersion), expectedVersion) },
            updateModel
        )
        log.trace("Updated {}Entity {}: {}", typeName, id, updated)
        val model = mapper.entityToModel(updated)
        log.trace("Built {} {}: {}", typeName, id, model)
        return model
    }

    /**
     * Default implementation for deleting an entity with given id
     *
     * @param id Id of the entity to delete
     */
    @Transactional
    open fun delete(id: I) {
        log.info("Deleting {} {}", typeName, id)
        val entity =
            repository.findByIdAndDeletedAtIsNull(id) ?: throw CRUDErrorException.notFound(typeName, id)
        log.trace("Found {}Entity {} to delete: {}", typeName, id, entity)
        val deleted = entity.markAsDeleted(Instant.now(clock))
        log.trace("Built {}Entity {} to delete: {}", typeName, id, deleted)
        val expectedVersion = deleted.version ?: 0
        persist(
            { assertSingleRowIsAffected(repository.updateByVersion(deleted, expectedVersion), expectedVersion) },
            "with id $id"
        )
        log.trace("Deleted {}Entity {}", typeName, id)
    }

    /**
     * Performs given persisting action
     *
     * This also flushes the changes made to the entity and performs a duplicate check.
     *
     * @param A      Type of result persisting action returns
     * @param action Persisting action to perform
     * @param data   Data describing the entity, used to build an error if there is a duplicate
     * @return Result of the persisting action
     */
    @Transactional
    protected open fun <A> persist(action: Supplier<A>, data: Any): A {
        return try {
            val a = action.get()
            repository.flush()
            a
        } catch (t: Throwable) {
            val cause = NestedExceptionUtils.getMostSpecificCause(t)
            if (cause is CRUDErrorException) {
                throw cause
            }
            if (cause is SQLException && cause.message?.contains("duplicate") == true) {
                throw CRUDErrorException.alreadyExists(typeName, data)
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
