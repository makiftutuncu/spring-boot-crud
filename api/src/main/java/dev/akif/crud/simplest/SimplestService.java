package dev.akif.crud.simplest;

import dev.akif.crud.CRUDService;

import java.io.Serializable;
import java.time.Clock;

/**
 * The simplest variant of {@link CRUDService} where models are entities directly
 * <p>
 * This is meant to be extended from a {@link org.springframework.stereotype.Service} class.
 *
 * @param <I>      Id type of the data
 * @param <E>      Entity type of the data which is a {@link SimplestEntity}
 * @param <Mapper> Mapper type of the data which is a {@link SimplestMapper}
 */
public abstract class SimplestService<
        I extends Serializable,
        E extends SimplestEntity<I, E>,
        Mapper extends SimplestMapper<I, E>> extends CRUDService<I, E, E, E, E, Mapper> {
    /**
     * Constructor to provide type name and dependencies to this service
     *
     * @param type       Type name of the data this service manages
     * @param clock      {@link Clock} dependency of this service
     * @param repository Repository dependency of this service
     * @param mapper     Mapper dependency of this service
     */
    protected SimplestService(final String type, final Clock clock, final SimplestRepository<I, E> repository, final Mapper mapper) {
        super(type, clock, repository, mapper);
    }
}
