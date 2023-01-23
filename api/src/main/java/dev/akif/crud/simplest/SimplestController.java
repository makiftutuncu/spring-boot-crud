package dev.akif.crud.simplest;

import dev.akif.crud.CRUDController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * The simplest variant of {@link CRUDController} where the only data model is the entity
 * <p>
 * This variant is only recommended for very simple data models.
 * Whenever possible, try and use one of the other variants.
 * <p>
 * This is meant to be extended from a @{@link RestController} class,
 * ideally also with a @{@link RequestMapping} with some path prefix for the endpoints.
 *
 * @param <I>      Id type of the data
 * @param <E>      Entity type of the data which is a {@link SimplestEntity}
 * @param <Mapper> Mapper type of the data which is a {@link SimplestMapper}
 * @param <S>      Service type of the data which is a {@link SimplestService}
 */
public abstract class SimplestController<
        I extends Serializable,
        E extends SimplestEntity<I, E>,
        Mapper extends SimplestMapper<I, E>,
        S extends SimplestService<I, E, Mapper>> extends CRUDController<I, E, E, E, E, E, E, E, Mapper, Mapper, S> {
    /**
     * Constructor to provide type name and dependencies to this controller
     *
     * @param type    Type name of the data this controller manages
     * @param service Service dependency of this controller
     * @param mapper  Mapper dependency of this controller
     */
    protected SimplestController(final String type, final S service, final Mapper mapper) {
        super(type, service, mapper);
    }
}
