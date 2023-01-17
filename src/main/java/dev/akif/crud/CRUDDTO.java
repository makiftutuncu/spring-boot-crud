package dev.akif.crud;

import java.io.Serializable;
import java.time.Instant;

/**
 * Base DTO of an entity
 *
 * @param <I> Id type of the data
 */
public interface CRUDDTO<I extends Serializable> {
    /**
     * Gets identifier of this DTO
     *
     * @return Identifier of this DTO
     */
    I id();

    /**
     * Gets the instant at which this DTO is created
     *
     * @return The instant at which this DTO is created
     */
    Instant createdAt();

    /**
     * Gets the instant at which this DTO is last updated
     *
     * @return The instant at which this DTO is last updated
     */
    Instant updatedAt();
}
