package dev.akif.crud.simple;

import dev.akif.crud.CRUDController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * Simple variant of {@link CRUDController} where create/update models are just models
 * and create/update DTOs are just DTOs directly
 * <p>
 * This is meant to be extended from a @{@link RestController} class,
 * ideally also with a @{@link RequestMapping} with some path prefix for the endpoints.
 *
 * @param <I>      Id type of the data
 * @param <E>      Entity type of the data which is a {@link SimpleEntity}
 * @param <M>      Model type of the data which is a {@link SimpleModel}
 * @param <D>      DTO type of the data which is a {@link SimpleDTO}
 * @param <Mapper> Mapper type of the data which is a {@link SimpleMapper}
 * @param <S>      Service type of the data which is a {@link SimpleService}
 */
public abstract class SimpleController<
        I extends Serializable,
        E extends SimpleEntity<I, E>,
        M extends SimpleModel<I, E>,
        D extends SimpleDTO<I, M, E>,
        Mapper extends SimpleMapper<I, E, M, D>,
        S extends SimpleService<I, E, M, D, Mapper>> extends CRUDController<I, E, M, D, M, M, D, D, Mapper, Mapper, S> {
    /**
     * Constructor to provide type name and dependencies to this controller
     *
     * @param type    Type name of the data this controller manages
     * @param service Service dependency of this controller
     * @param mapper  Mapper dependency of this controller
     */
    protected SimpleController(final String type, final S service, final Mapper mapper) {
        super(type, service, mapper);
    }
}
