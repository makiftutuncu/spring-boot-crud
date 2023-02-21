package dev.akif.crud.common

import java.time.Clock
import java.time.Instant

/**
 * Interface to provide the current instant
 */
interface InstantProvider {
    /**
     * Current instant
     */
    val now: Instant

    /** @suppress */
    companion object {
        /**
         * Builds an [InstantProvider] that uses given [Clock] to provide the current instant
         *
         * @param clock Clock
         * @return [InstantProvider] that uses given [Clock] to provide the current instant
         */
        @JvmStatic
        fun clockBased(clock: Clock): InstantProvider = object : InstantProvider {
            override val now: Instant = clock.instant()
        }

        /**
         * Builds an [InstantProvider] that uses [Clock.systemUTC] to provide the current instant
         *
         * @return [InstantProvider] that uses [Clock.systemUTC] to provide the current instant
         */
        @JvmStatic
        val utc: InstantProvider = clockBased(Clock.systemUTC())
    }
}
