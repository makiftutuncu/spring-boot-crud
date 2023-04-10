package dev.akif.crud

import jakarta.persistence.MappedSuperclass
import org.hibernate.Hibernate
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
abstract class CRUDEntity<I : Serializable> {
    abstract var id: I?
    abstract var version: Int?
    abstract var createdAt: Instant?
    abstract var updatedAt: Instant?
    abstract var deletedAt: Instant?

    /** @suppress */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        if (other !is CRUDEntity<*>) return false
        return id == other.id
    }

    /** @suppress */
    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}
