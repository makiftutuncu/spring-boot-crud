package dev.akif.crud;

import java.io.Serializable;

/**
 * Base model that contains required data to update an existing entity
 *
 * @param <I> Id type of the data
 * @param <E> Entity type of the data
 */
public interface CRUDUpdateModel<I extends Serializable, E extends CRUDEntity<I, E>> {
    /**
     * Applies updates contained in this model to given entity
     *
     * @param entity Entity to update
     * @return Updated entity
     */
    E updatingFieldsOf(final E entity);
}
