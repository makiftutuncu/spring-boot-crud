package dev.akif.crud

import java.io.Serializable
import java.util.UUID
import kotlin.random.Random

/**
 * Interface for generating ids in tests
 *
 * @param I Id type
 */
interface IdGenerator<I : Serializable> {
    /**
     * Generate the next id
     *
     * @return Generated id
     */
    fun next(): I

    /**
     * Generate a random id
     *
     * @return Generated id
     */
    fun random(): I

    /** @suppress */
    companion object {
        /**
         * [IdGenerator] for [UUID] ids
         */
        @JvmField
        val uuid: IdGenerator<UUID> = object: IdGenerator<UUID> {
            override fun next(): UUID = random()
            override fun random(): UUID = UUID.randomUUID()
        }

        /**
         * Builds a sequential [IdGenerator] for [Long] ids
         *
         * @param start Starting value of the ids
         * @return Built [IdGenerator]
         */
        @JvmStatic
        fun sequential(start: Long): IdGenerator<Long> = object : IdGenerator<Long> {
            private var next: Long = start

            override fun next(): Long {
                next++
                return next
            }

            override fun random(): Long =
                Random.nextLong()
        }
    }
}
