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

@Slf4j
public abstract class CRUDService<
        I extends Serializable,
        M extends CRUDModel<I>,
        E extends CRUDEntity<I, M>,
        CM extends CRUDCreateModel<I, M, E>,
        UM extends CRUDUpdateModel<I, M, E>,
        R extends CRUDRepository<I, M, E>> {
    public final String modelName;

    protected final Clock clock;
    protected final R repository;

    protected CRUDService(final String modelName, final Clock clock, final R repository) {
        this.modelName = modelName;
        this.clock = clock;
        this.repository = repository;
    }

    @Transactional
    public M create(final CM createModel) {
        log.info("Creating new {}: {}", modelName, createModel);

        final var entity = createModel.toEntity();
        log.trace("Built {}Entity: {}", modelName, entity);

        final var saved = save(entity, createModel);
        log.trace("Saved {}Entity: {}", modelName, saved);

        final var model = saved.toModel();
        log.trace("Built {}: {}", modelName, model);

        return model;
    }

    public Page<M> getAll(final Pageable pageable) {
        log.info("Getting {} {}", modelName, pageable);

        final var entities = repository.findAllByDeleted(pageable, false);
        log.trace("Found {}Entity {}: {}", modelName, pageable, entities.getContent());

        final var models = entities.map(CRUDEntity::toModel);
        log.trace("Built {} {}: {}", modelName, pageable, models.getContent());

        return models;
    }

    public Optional<M> get(final I id) {
        log.info("Getting {} {}", modelName, id);

        final var entity = repository.findByIdAndDeleted(id, false);
        entity.ifPresent(e -> log.trace("Found {}Entity {}: {}", modelName, id, e));

        return entity.map(e -> {
            final var m = e.toModel();
            log.trace("Built {} {}: {}", modelName, id, m);
            return m;
        });
    }

    @Transactional
    public M update(final I id, final UM updateModel) {
        log.info("Updating {} {}: {}", modelName, id, updateModel);

        final var entity = repository.findByIdAndDeleted(id, false).orElseThrow(() -> CRUDError.notFound(modelName, id));
        log.trace("Found {}Entity {} to update: {}", modelName, id, entity);

        updateModel.applyUpdatesTo(entity);
        entity.setUpdatedAt(Instant.now(clock));
        log.trace("Built {}Entity {} to update: {}", modelName, id, entity);

        final var updated = save(entity, updateModel);
        log.trace("Updated {}Entity {}: {}", modelName, id, updated);

        final var model = updated.toModel();
        log.trace("Built {} {}: {}", modelName, id, model);

        return model;
    }

    @Transactional
    public void delete(final I id) {
        log.info("Deleting {} {}", modelName, id);

        final var entity = repository.findByIdAndDeleted(id, false).orElseThrow(() -> CRUDError.notFound(modelName, id));
        log.trace("Found {}Entity {} to delete: {}", modelName, id, entity);

        entity.setDeleted(true);
        log.trace("Built {}Entity {} to update: {}", modelName, id, entity);

        final var deleted = repository.save(entity);
        log.trace("Deleted {}Entity {}: {}", modelName, id, deleted);
    }

    @Transactional
    protected <D> E save(final E entity, final D data) {
        try {
            final var e = repository.save(entity);
            repository.flush();
            return e;
        } catch (final Throwable t) {
            if (NestedExceptionUtils.getMostSpecificCause(t) instanceof SQLException e && e.toString().contains("duplicate")) {
                throw CRUDError.alreadyExists(modelName, data);
            }
            throw t;
        }
    }
}
