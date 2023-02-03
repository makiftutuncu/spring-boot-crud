package dev.akif.crud.simple

import dev.akif.crud.CRUDEntity
import java.io.Serializable
import java.time.Instant

/**
 * Simple variant of [CRUDEntity]
 *
 * @param I Id type of the data
 * @param E Entity type which is a [CRUDEntity], meant to be the exact type extending this class
 */
abstract class SimpleEntity<I : Serializable, E : SimpleEntity<I, E>>(
    override var id: I?,
    override var version: Int?,
    override var createdAt: Instant?,
    override var updatedAt: Instant?,
    override var deletedAt: Instant?
) : CRUDEntity<I, E>(id, version, createdAt, updatedAt, deletedAt)
