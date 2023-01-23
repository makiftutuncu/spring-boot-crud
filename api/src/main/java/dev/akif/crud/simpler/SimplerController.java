package dev.akif.crud.simpler;

import dev.akif.crud.CRUDController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * Simpler variant of {@link CRUDController} where create/update models and DTOs are just models
 * <p>
 * This is meant to be extended from a @{@link RestController} class,
 * ideally also with a @{@link RequestMapping} with some path prefix for the endpoints.
 *
 * @param <I>      Id type of the data
 * @param <E>      Entity type of the data which is a {@link SimplerEntity}
 * @param <M>      Model type of the data which is a {@link SimplerModel}
 * @param <Mapper> Mapper type of the data which is a {@link SimplerMapper}
 * @param <S>      Service type of the data which is a {@link SimplerService}
 */
public abstract class SimplerController<
        I extends Serializable,
        E extends SimplerEntity<I, E>,
        M extends SimplerModel<I, E, M>,
        Mapper extends SimplerMapper<I, E, M>,
        S extends SimplerService<I, E, M, Mapper>> extends CRUDController<I, E, M, M, M, M, M, M, Mapper, Mapper, S> {
    /**
     * Constructor to provide type name and dependencies to this controller
     *
     * @param type    Type name of the data this controller manages
     * @param service Service dependency of this controller
     * @param mapper  Mapper dependency of this controller
     */
    protected SimplerController(final String type, final S service, final Mapper mapper) {
        super(type, service, mapper);
    }
}
