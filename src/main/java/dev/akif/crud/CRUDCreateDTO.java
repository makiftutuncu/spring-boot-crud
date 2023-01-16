package dev.akif.crud;

import java.io.Serializable;

/**
 * Base DTO that contains required data to create a new entity
 *
 * @param <I>  Id type of the data
 * @param <E>  Entity type of the data
 * @param <CM> Create model type of the data
 */
public interface CRUDCreateDTO<I extends Serializable, E extends CRUDEntity<I, E>, CM extends CRUDCreateModel<I, E>> {
    /**
     * Mapper to convert this create DTO into a create model
     *
     * @return Create model built from this create DTO
     */
    CM withFieldsToCreate();
}
