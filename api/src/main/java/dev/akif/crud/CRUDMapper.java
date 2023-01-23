package dev.akif.crud;

import java.io.Serializable;
import java.time.Instant;

/**
 * Mapper to convert between models and entities
 *
 * @param <I>  Id type of the data
 * @param <E>  Entity type of the data
 * @param <M>  Model type of the data
 * @param <CM> Create model type of the data
 * @param <UM> Update model type of the data
 */
public interface CRUDMapper<
        I extends Serializable,
        E extends CRUDEntity<I, E>,
        M extends CRUDModel<I>,
        CM extends CRUDCreateModel<I, E>,
        UM extends CRUDUpdateModel<I, E>> {
    /**
     * Mapper to convert given model into an entity to be created
     *
     * @param createModel Create model to convert
     * @param now         Current instant
     * @return Entity to be created, built from this model
     */
    E entityToBeCreatedFrom(final CM createModel, final Instant now);

    /**
     * Mapper to convert from given entity to model
     *
     * @param entity Entity to convert
     * @return Model built from given entity
     */
    M entityToModel(final E entity);

    /**
     * Applies updates contained in given update model to given entity
     *
     * @param updateModel Update model to apply
     * @param entity      Entity to update
     * @return Updated entity
     */
    E updateEntityWith(final UM updateModel, final E entity);
}
