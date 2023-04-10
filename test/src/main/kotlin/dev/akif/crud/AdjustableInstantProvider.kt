package dev.akif.crud

import dev.akif.crud.common.InstantProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.temporal.ChronoField

/**
 * Implementation of [InstantProvider] that is adjustable in milliseconds by the use of [adjust] to simulate changes in time
 */
class AdjustableInstantProvider(
    private var instant: Instant = Instant.now().with(ChronoField.NANO_OF_SECOND, 0),
    private var adjustmentMillis: Long = 0L
) : InstantProvider {
    override fun now(): Instant = instant.plusMillis(adjustmentMillis)

    /**
     * Adjusts the time by given function, [now] will start returning the adjusted instant after calling this
     *
     * @param f Function to adjust the time
     */
    fun adjust(f: (Instant) -> Instant) {
        val before = now()
        val after = f(now())
        adjustmentMillis = after.toEpochMilli() - before.toEpochMilli()
        log.debug("Adjusted instant provider by ${adjustmentMillis}ms, it was $before and it is now $after")
    }

    /**
     * Resets the adjustments made to this instant provider
     */
    fun reset() {
        adjustmentMillis = 0
        log.debug("Reset instant provider, it is now ${now()}")
    }

    /** @suppress */
    companion object {
        private val log: Logger = LoggerFactory.getLogger(AdjustableInstantProvider::class.java)
    }
}
