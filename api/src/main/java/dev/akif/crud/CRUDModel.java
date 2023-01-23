package dev.akif.crud;

import java.io.Serializable;
import java.time.Instant;

/**
 * Base model of a data model
 *
 * @param <I> Id type of the data
 */
public interface CRUDModel<I extends Serializable> {
    /**
     * Gets identifier of this model
     *
     * @return Identifier of this model
     */
    I id();

    /**
     * Gets version of this model
     *
     * @return Version of this model
     */
    int version();

    /**
     * Gets the instant at which this model is created
     *
     * @return The instant at which this model is created
     */
    Instant createdAt();

    /**
     * Gets the instant at which this model is last updated
     *
     * @return The instant at which this model is last updated
     */
    Instant updatedAt();

    /**
     * Gets the instant at which this model is deleted
     *
     * @return The instant at which this model is deleted or null if it is not deleted
     */
    Instant deletedAt();
}
