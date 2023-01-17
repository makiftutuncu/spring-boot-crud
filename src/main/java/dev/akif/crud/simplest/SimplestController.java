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
 * @param <I>  Id type of the data
 * @param <E>  Entity type of the data which is a {@link SimplestEntity}
 * @param <R>  Repository type of the data which is a {@link SimplestRepository}
 * @param <S>  Service type of the data which is a {@link SimplestService}
 */
public abstract class SimplestController<
        I extends Serializable,
        E extends SimplestEntity<I, E>,
        R extends SimplestRepository<I, E>,
        S extends SimplestService<I, E, R>> extends CRUDController<I, E, E, E, E, E, E, E, R, S> {
    /**
     * Constructor to provide type name and dependencies to this controller
     *
     * @param type    Type name of the data this controller manages
     * @param service Service dependency of this controller
     */
    protected SimplestController(final String type, final S service) {
        super(type, service);
    }

    @Override
    protected E toDTO(final E model) {
        return model;
    }
}
