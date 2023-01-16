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
 * @param <I>  Id type of the data
 * @param <E>  Entity type of the data which is a {@link SimplerEntity}
 * @param <M>  Model type of the data which is a {@link SimplerModel}
 * @param <R>  Repository type of the data which is a {@link SimplerRepository}
 * @param <S>  Service type of the data which is a {@link SimplerService}
 */
public abstract class SimplerController<
        I extends Serializable,
        E extends SimplerEntity<I, E>,
        M extends SimplerModel<I, E, M>,
        R extends SimplerRepository<I, E>,
        S extends SimplerService<I, E, M, R>> extends CRUDController<I, E, M, M, M, M, M, M, R, S> {
    /**
     * Constructor to provide type name and dependencies to this controller
     *
     * @param type    Type name of the data this controller manages
     * @param service Service dependency of this controller
     */
    protected SimplerController(final String type, final S service) {
        super(type, service);
    }

    @Override
    protected M toDTO(M model) {
        return model;
    }
}
