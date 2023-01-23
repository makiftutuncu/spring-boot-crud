package dev.akif.crud;

import java.io.Serializable;

/**
 * Mapper to convert between models and entities in tests
 *
 * @param <I>  Id type of the data
 * @param <E>  Entity type of the data
 * @param <M>  Model type of the data
 * @param <CM> Create model type of the data
 * @param <UM> Update model type of the data
 */
public interface CRUDTestMapper<
        I extends Serializable,
        E extends CRUDEntity<I, E>,
        M extends CRUDModel<I>,
        CM extends CRUDCreateModel<I, E>,
        UM extends CRUDUpdateModel<I, E>> {
    /**
     * Mapper to convert from given entity to create model
     *
     * @param entity Entity to convert
     * @return Create model built from given entity
     */
    CM entityToCreateModel(final E entity);

    /**
     * Mapper to convert from given entity to model replacing its id with given expected id
     *
     * @param entity     Entity to convert
     * @param expectedId Expected id to with which to replace actual id
     * @return Model built from given entity with expected id
     */
    M entityToModelWithExpectedId(final E entity, final I expectedId);
}
