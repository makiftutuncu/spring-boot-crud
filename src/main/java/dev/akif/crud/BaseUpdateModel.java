package dev.akif.crud;

import java.io.Serializable;

/**
 * Base model that contains required data to update an existing entity
 *
 * @param <I> Id type of the data
 * @param <M> Model type of the data
 * @param <E> Entity type of the data
 */
public interface BaseUpdateModel<
        I extends Serializable,
        M extends BaseModel<I>,
        E extends BaseEntity<I, M>> {
    /**
     * Applies updates contained in this model to given entity
     *
     * @param entity Entity to update
     */
    void applyUpdatesTo(final E entity);
}
