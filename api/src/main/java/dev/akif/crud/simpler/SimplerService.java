package dev.akif.crud.simpler;

import dev.akif.crud.CRUDService;

import java.io.Serializable;
import java.time.Clock;

/**
 * Simpler variant of {@link CRUDService} where create/update models are just models directly
 * <p>
 * This is meant to be extended from a {@link org.springframework.stereotype.Service} class.
 *
 * @param <I>      Id type of the data
 * @param <M>      Model type of the data which is a {@link SimplerModel}
 * @param <E>      Entity type of the data which is a {@link SimplerEntity}
 * @param <Mapper> Mapper of the data which is a {@link SimplerMapper}
 */
public abstract class SimplerService<
        I extends Serializable,
        E extends SimplerEntity<I, E>,
        M extends SimplerModel<I, E, M>,
        Mapper extends SimplerMapper<I, E, M>> extends CRUDService<I, M, E, M, M, Mapper> {
    /**
     * Constructor to provide type name and dependencies to this service
     *
     * @param type       Type name of the data this service manages
     * @param clock      {@link Clock} dependency of this service
     * @param repository Repository dependency of this service
     * @param mapper     Mapper dependency of this service
     */
    protected SimplerService(final String type, final Clock clock, final SimplerRepository<I, E> repository, final Mapper mapper) {
        super(type, clock, repository, mapper);
    }
}
