package dev.akif.crud;

import java.io.Serializable;

/**
 * Base model that contains required data to create a new entity
 *
 * @param <I> Id type of the data
 * @param <M> Model type of the data
 * @param <E> Entity type of the data
 */
public interface BaseCreateModel<
        I extends Serializable,
        M extends BaseModel<I>,
        E extends BaseEntity<I, M>> {
    /**
     * Mapper to convert this model into an entity to be created
     *
     * @return Entity to be created, built from this model
     */
    E toEntity();
}
