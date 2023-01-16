package dev.akif.crud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.sql.SQLException;
import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.function.Supplier;

import static dev.akif.crud.error.CRUDErrorException.alreadyExists;
import static dev.akif.crud.error.CRUDErrorException.notFound;

/**
 * Base implementation of a CRUD service for business layer
 * <p>
 * This is meant to be extended from a {@link org.springframework.stereotype.Service} class.
 *
 * @param <I>  Id type of the data
 * @param <M>  Model type of the data
 * @param <E>  Entity type of the data
 * @param <CM> Create model type of the data
 * @param <UM> Update model type of the data
 * @param <R>  Repository type of the data
 */
@Slf4j
public abstract class CRUDService<
        I extends Serializable,
        M extends CRUDModel<I>,
        E extends CRUDEntity<I, E>,
        CM extends CRUDCreateModel<I, E>,
        UM extends CRUDUpdateModel<I, E>,
        R extends CRUDRepository<I, E>> {
    /**
     * Type name of the data this service manages
     */
    protected final String type;
    /**
     * {@link Clock} dependency of this service
     */
    protected final Clock clock;
    /**
     * Repository dependency of this service
     */
    protected final R repository;

    /**
     * Constructor to provide type name and dependencies to this service
     *
     * @param type       Type name of the data this service manages
     * @param clock      {@link Clock} dependency of this service
     * @param repository Repository dependency of this service
     */
    protected CRUDService(final String type, final Clock clock, final R repository) {
        this.type = type;
        this.clock = clock;
        this.repository = repository;
    }

    /**
     * Mapper to convert from entity to model
     *
     * @param entity Entity to convert
     * @return Model built from given entity
     */
    protected abstract M toModel(final E entity);

    /**
     * Default implementation for creating a new entity from given create model
     *
     * @param createModel Create model containing data of the entity to create
     * @return Model of the created entity
     */
    @Transactional
    public M create(final CM createModel) {
        log.info("Creating new {}: {}", type, createModel);

        final var entity = createModel.withFieldsToCreate(Instant.now(clock));
        log.trace("Built {}Entity: {}", type, entity);

        final var saved = persist(() -> repository.save(entity), createModel);
        log.trace("Saved {}Entity: {}", type, saved);

        final var model = toModel(saved);
        log.trace("Built {}: {}", type, model);

        return model;
    }

    /**
     * Default implementation for listing entities with given pagination
     *
     * @param pageable {@link Pageable}
     * @return {@link Page} of models of entities
     */
    public Page<M> getAll(final Pageable pageable) {
        log.info("Getting {} {}", type, pageable);

        final var entities = repository.findAllByDeletedAtIsNull(pageable);
        log.trace("Found {}Entity {}: {}", type, pageable, entities.getContent());

        final var models = entities.map(this::toModel);
        log.trace("Built {} {}: {}", type, pageable, models.getContent());

        return models;
    }

    /**
     * Default implementation for getting an entity with given id
     *
     * @param id Id of the entity
     * @return Model of the entity with given id
     */
    public Optional<M> get(final I id) {
        log.info("Getting {} {}", type, id);

        final var entity = repository.findByIdAndDeletedAtIsNull(id);
        entity.ifPresent(e -> log.trace("Found {}Entity {}: {}", type, id, e));

        return entity.map(e -> {
            final var m = toModel(e);
            log.trace("Built {} {}: {}", type, id, m);
            return m;
        });
    }

    /**
     * Default implementation for updating an entity with given id with given update model data
     *
     * @param id          Id of the entity to update
     * @param updateModel Update model containing data to be updated
     * @return Model of the updated entity
     */
    @Transactional
    public M update(final I id, final UM updateModel) {
        log.info("Updating {} {}: {}", type, id, updateModel);

        final var entity = repository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> notFound(type, id));
        log.trace("Found {}Entity {} to update: {}", type, id, entity);

        final var updated = updateModel.updatingFieldsOf(entity).updatedNow(Instant.now(clock));
        log.trace("Built {}Entity {} to update: {}", type, id, updated);

        final var expectedVersion = updated.getVersion();
        persist(
                () -> assertSingleRowIsAffected(repository.updateByVersion(updated, expectedVersion), expectedVersion),
                updateModel
        );
        log.trace("Updated {}Entity {}: {}", type, id, updated);

        final var model = toModel(updated);
        log.trace("Built {} {}: {}", type, id, model);

        return model;
    }

    /**
     * Default implementation for deleting an entity with given id
     *
     * @param id Id of the entity to delete
     */
    @Transactional
    public void delete(final I id) {
        log.info("Deleting {} {}", type, id);

        final var entity = repository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> notFound(type, id));
        log.trace("Found {}Entity {} to delete: {}", type, id, entity);

        final var deleted = entity.markAsDeleted(Instant.now(clock));
        log.trace("Built {}Entity {} to delete: {}", type, id, deleted);

        final var expectedVersion = deleted.getVersion();
        persist(
                () -> assertSingleRowIsAffected(repository.updateByVersion(deleted, expectedVersion), expectedVersion),
                "with id " + id
        );
        log.trace("Deleted {}Entity {}", type, id);
    }

    /**
     * Performs given persisting action
     * <p>
     * This also flushes the changes made to the entity and performs a duplicate check.
     *
     * @param action Persisting action to perform
     * @param data   Data describing the entity, used to build an error if there is a duplicate
     * @param <A>    Type of result persisting action returns
     * @param <D>    Type of the data describing the entity
     * @return Result of the persisting action
     */
    @Transactional
    protected <A, D> A persist(final Supplier<A> action, final D data) {
        try {
            final var a = action.get();
            repository.flush();
            return a;
        } catch (final Throwable t) {
            if (NestedExceptionUtils.getMostSpecificCause(t) instanceof SQLException e && e.toString().contains("duplicate")) {
                throw alreadyExists(type, data);
            }
            throw t;
        }
    }

    private int assertSingleRowIsAffected(final int affected, final int expected) {
        if (affected != 1) {
            throw new IllegalStateException("Cannot update %sEntity, entity version wasn't %d!".formatted(type, expected));
        }
        return affected;
    }
}
