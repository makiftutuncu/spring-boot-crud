package dev.akif.crud;

import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;
import java.time.Instant;

/**
 * Base entity of a data model representing its persisted structure
 *
 * @param <I> Id type of the data
 * @param <E> Entity type, meant to be the exact type extending this class
 */
@MappedSuperclass
public abstract class CRUDEntity<I extends Serializable, E extends CRUDEntity<I, E>> {
    /**
     * Default empty constructor
     */
    public CRUDEntity() {}

    /**
     * Gets identifier of this entity
     *
     * @return Identifier of this entity
     */
    public abstract I getId();

    /**
     * Sets identifier of this entity
     *
     * @param id New id
     */
    public abstract void setId(final I id);

    /**
     * Gets version of this entity used in optimistic locking during persistence
     *
     * @return Version of this entity used in optimistic locking during persistence
     */
    public abstract int getVersion();

    /**
     * Sets version of this entity used in optimistic locking during persistence
     *
     * @param version New version
     */
    public abstract void setVersion(final int version);

    /**
     * Gets the instant at which this entity is created
     *
     * @return The instant at which this entity is created
     */
    public abstract Instant getCreatedAt();

    /**
     * Sets the instant at which this entity is created
     *
     * @param createdAt New created at
     */
    public abstract void setCreatedAt(final Instant createdAt);

    /**
     * Gets the instant at which this entity is last updated
     *
     * @return The instant at which this entity is last updated
     */
    public abstract Instant getUpdatedAt();

    /**
     * Sets the instant at which this entity is last updated
     *
     * @param updatedAt New updated at
     */
    public abstract void setUpdatedAt(final Instant updatedAt);

    /**
     * Gets the instant at which this entity is logically deleted or null if is not deleted
     *
     * @return The instant at which this entity is logically deleted or null if is not deleted
     * <p>
     * Deleted entities should be treated as if they don't exist at all.
     */
    public abstract Instant getDeletedAt();

    /**
     * Sets the instant at which this entity is logically deleted
     *
     * @param deletedAt New deleted at
     */
    public abstract void setDeletedAt(final Instant deletedAt);

    /**
     * Marks this entity as updated at given time, also incrementing its version
     *
     * @param now Instant at which this entity is updated
     *
     * @return Updated entity
     */
    @SuppressWarnings("unchecked")
    public E updatedNow(final Instant now) {
        setVersion(getVersion() + 1);
        setUpdatedAt(now);
        return (E) this;
    }

    /**
     * Marks this entity as deleted at given time, also setting updated at and incrementing its version
     *
     * @param now Instant at which this entity is deleted
     *
     * @return Deleted entity
     */
    @SuppressWarnings("unchecked")
    public E markAsDeleted(final Instant now) {
        setVersion(getVersion() + 1);
        setUpdatedAt(now);
        setDeletedAt(now);
        return (E) this;
    }
}
