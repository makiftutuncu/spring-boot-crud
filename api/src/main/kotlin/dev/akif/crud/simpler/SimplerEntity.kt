package dev.akif.crud.simpler

import dev.akif.crud.CRUDEntity
import java.io.Serializable
import java.time.Instant

/**
 * Simpler variant of [CRUDEntity]
 *
 * @param I Id type of the data
 */
abstract class SimplerEntity<I : Serializable>(
    override var id: I? = null,
    override var version: Int? = null,
    override var createdAt: Instant? = null,
    override var updatedAt: Instant? = null,
    override var deletedAt: Instant? = null,
) : CRUDEntity<I>(id, version, createdAt, updatedAt, deletedAt)
