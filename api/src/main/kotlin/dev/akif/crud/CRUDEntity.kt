package dev.akif.crud

import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import java.io.Serializable
import java.time.Instant

/**
 * Base entity of a data model representing its persisted structure
 *
 * @param I Id type of the data
 *
 * @property id        Identifier of this entity
 * @property version   Version of this entity used in optimistic locking during persistence
 * @property createdAt Instant at which this entity is created
 * @property updatedAt Instant at which this entity is last updated
 * @property deletedAt Instant at which this entity is logically deleted or null if is not deleted,
 * deleted entities should be treated as if they don't exist at all
 */
@MappedSuperclass
abstract class CRUDEntity<I : Serializable>(
    @Id open var id: I? = null,
    open var version: Int? = null,
    open var createdAt: Instant? = null,
    open var updatedAt: Instant? = null,
    open var deletedAt: Instant? = null
) {
    /** @suppress */
    override fun toString(): String =
        "CRUDEntity(id=$id, version=$version, createdAt=$createdAt, updatedAt=$updatedAt, deletedAt=$deletedAt)"
}
