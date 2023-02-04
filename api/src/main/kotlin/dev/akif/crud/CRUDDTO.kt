package dev.akif.crud

import java.io.Serializable
import java.time.Instant

/**
 * Base DTO of an entity
 *
 * @param I Id type of the data
 */
interface CRUDDTO<out I : Serializable> {
    /**
     * Gets identifier of this DTO
     *
     * @return Identifier of this DTO
     */
    fun id(): I

    /**
     * Gets the instant at which this DTO is created
     *
     * @return The instant at which this DTO is created
     */
    fun createdAt(): Instant

    /**
     * Gets the instant at which this DTO is last updated
     *
     * @return The instant at which this DTO is last updated
     */
    fun updatedAt(): Instant
}
