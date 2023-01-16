package dev.akif.crud;

import java.io.Serializable;
import java.time.Instant;

/**
 * Base model that contains required data to create a new entity
 *
 * @param <I> Id type of the data
 * @param <E> Entity type of the data
 */
public interface CRUDCreateModel<I extends Serializable, E extends CRUDEntity<I, E>> {
    /**
     * Mapper to convert this model into an entity to be created
     *
     * @param now Current instant
     * @return Entity to be created, built from this model
     */
    E withFieldsToCreate(final Instant now);
}
