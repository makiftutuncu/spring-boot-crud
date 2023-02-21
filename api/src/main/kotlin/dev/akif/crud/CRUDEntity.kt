package dev.akif.crud

import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import java.io.Serializable
import java.time.Instant

/**
 * Base entity of a data model representing its persisted structure
 *
 * @param I Id type of the data
 * @param E Entity type which is a [CRUDEntity], meant to be the exact type extending this class
 *
 * @property id        Identifier of this entity
 * @property version   Version of this entity used in optimistic locking during persistence
 * @property createdAt Instant at which this entity is created
 * @property updatedAt Instant at which this entity is last updated
 * @property deletedAt Instant at which this entity is logically deleted or null if is not deleted,
 * deleted entities should be treated as if they don't exist at all
 */
@MappedSuperclass
abstract class CRUDEntity<I : Serializable, out E : CRUDEntity<I, E>>(
    @Id open var id: I?,
    open var version: Int?,
    open var createdAt: Instant?,
    open var updatedAt: Instant?,
    open var deletedAt: Instant?
) {
    /** @suppress */
    override fun toString(): String {
        return "CRUDEntity(id=$id, version=$version, createdAt=$createdAt, updatedAt=$updatedAt, deletedAt=$deletedAt)"
    }
}
