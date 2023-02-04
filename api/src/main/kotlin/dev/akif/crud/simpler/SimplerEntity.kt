package dev.akif.crud.simpler

import dev.akif.crud.CRUDEntity
import java.io.Serializable
import java.time.Instant

/**
 * Simpler variant of [CRUDEntity]
 *
 * @param I Id type of the data
 * @param E Entity type which is a [SimplerEntity], meant to be the exact type extending this class
 */
abstract class SimplerEntity<I : Serializable, E : SimplerEntity<I, E>>(
    override var id: I?,
    override var version: Int?,
    override var createdAt: Instant?,
    override var updatedAt: Instant?,
    override var deletedAt: Instant?
) : CRUDEntity<I, E>(id, version, createdAt, updatedAt, deletedAt)
