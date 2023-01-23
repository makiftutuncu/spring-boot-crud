package dev.akif.crud.simple;

import dev.akif.crud.CRUDService;

import java.io.Serializable;
import java.time.Clock;

/**
 * Simple variant of {@link CRUDService} where create/update models are just models directly
 * <p>
 * This is meant to be extended from a {@link org.springframework.stereotype.Service} class.
 *
 * @param <I>      Id type of the data
 * @param <M>      Model type of the data which is a {@link SimpleModel}
 * @param <E>      Entity type of the data which is a {@link SimpleEntity}
 * @param <D>      DTO type of the data which is a {@link SimpleDTO}
 * @param <Mapper> Mapper type of the data which is a {@link SimpleMapper}
 */
public abstract class SimpleService<
        I extends Serializable,
        E extends SimpleEntity<I, E>,
        M extends SimpleModel<I, E>,
        D extends SimpleDTO<I, M, E>,
        Mapper extends SimpleMapper<I, E, M, D>> extends CRUDService<I, M, E, M, M, Mapper> {
    /**
     * Constructor to provide type name and dependencies to this service
     *
     * @param type       Type name of the data this service manages
     * @param clock      {@link Clock} dependency of this service
     * @param repository Repository dependency of this service
     * @param mapper     Mapper dependency of this service
     */
    protected SimpleService(final String type, final Clock clock, final SimpleRepository<I, E> repository, final Mapper mapper) {
        super(type, clock, repository, mapper);
    }
}
