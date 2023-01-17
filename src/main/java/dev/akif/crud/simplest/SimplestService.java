package dev.akif.crud.simplest;

import dev.akif.crud.CRUDService;

import java.io.Serializable;
import java.time.Clock;

/**
 * The simplest variant of {@link CRUDService} where models are entities directly
 * <p>
 * This is meant to be extended from a {@link org.springframework.stereotype.Service} class.
 *
 * @param <I>  Id type of the data
 * @param <E>  Entity type of the data which is a {@link SimplestEntity}
 * @param <R>  Repository type of the data which is a {@link SimplestRepository}
 */
public abstract class SimplestService<
        I extends Serializable,
        E extends SimplestEntity<I, E>,
        R extends SimplestRepository<I, E>> extends CRUDService<I, E, E, E, E, R> {
    /**
     * Constructor to provide type name and dependencies to this service
     *
     * @param type       Type name of the data this service manages
     * @param clock      {@link Clock} dependency of this service
     * @param repository Repository dependency of this service
     */
    protected SimplestService(final String type, final Clock clock, final R repository) {
        super(type, clock, repository);
    }

    @Override
    protected E toModel(final E entity) {
        return entity;
    }
}
