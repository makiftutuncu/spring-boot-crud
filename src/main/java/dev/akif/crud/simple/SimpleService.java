package dev.akif.crud.simple;

import dev.akif.crud.CRUDService;

import java.io.Serializable;
import java.time.Clock;

/**
 * Simple variant of {@link CRUDService} where create/update models are just models directly
 * <p>
 * This is meant to be extended from a {@link org.springframework.stereotype.Service} class.
 *
 * @param <I>  Id type of the data
 * @param <M>  Model type of the data which is a {@link SimpleModel}
 * @param <E>  Entity type of the data which is a {@link SimpleEntity}
 * @param <R>  Repository type of the data which is a {@link SimpleRepository}
 */
public abstract class SimpleService<
        I extends Serializable,
        E extends SimpleEntity<I, E>,
        M extends SimpleModel<I, E>,
        R extends SimpleRepository<I, E>> extends CRUDService<I, M, E, M, M, R> {
    /**
     * Constructor to provide type name and dependencies to this service
     *
     * @param type       Type name of the data this service manages
     * @param clock      {@link Clock} dependency of this service
     * @param repository Repository dependency of this service
     */
    protected SimpleService(final String type, final Clock clock, final R repository) {
        super(type, clock, repository);
    }
}
