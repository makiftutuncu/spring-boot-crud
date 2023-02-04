package dev.akif.crud

import java.io.Serializable
import java.time.Instant

/**
 * Base model of a data model
 *
 * @param I Id type of the data
 */
interface CRUDModel<out I : Serializable> {
    /**
     * Gets identifier of this model
     *
     * @return Identifier of this model
     */
    fun id(): I

    /**
     * Gets version of this model
     *
     * @return Version of this model
     */
    fun version(): Int

    /**
     * Gets the instant at which this model is created
     *
     * @return The instant at which this model is created
     */
    fun createdAt(): Instant

    /**
     * Gets the instant at which this model is last updated
     *
     * @return The instant at which this model is last updated
     */
    fun updatedAt(): Instant

    /**
     * Gets the instant at which this model is deleted
     *
     * @return The instant at which this model is deleted or null if it is not deleted
     */
    fun deletedAt(): Instant?
}
