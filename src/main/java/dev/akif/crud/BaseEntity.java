package dev.akif.crud;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;

/**
 * Base entity of a data model representing its persisted structure
 *
 * @param <I> Id type of the data
 * @param <M> Model type of the data
 */
@EntityListeners(AuditingEntityListener.class)
@Getter
@MappedSuperclass
@Setter
@ToString
public abstract class BaseEntity<I extends Serializable, M extends BaseModel<I>> {
    /**
     * Identifier of this entity
     */
    @Id
    protected I id;

    /**
     * Version of this entity used in optimistic locking during persistence
     */
    @Version
    protected int version;

    /**
     * The instant at which this entity is created
     */
    @CreatedDate
    protected Instant createdAt;

    /**
     * The instant at which this entity is last updated
     */
    @LastModifiedDate
    protected Instant updatedAt;

    /**
     * Whether this entity is logically deleted
     * <p>
     * Deleted entities should be treated as if they don't exist at all.
     */
    protected boolean deleted;

    /**
     * Constructor to create an entity with given data
     *
     * @param id        Identifier of this entity
     * @param version   Version of this entity
     * @param createdAt The instant at which this entity is created
     * @param updatedAt The instant at which this entity is last updated
     * @param deleted   Whether this entity is logically deleted
     */
    protected BaseEntity(
            final I id,
            final int version,
            final Instant createdAt,
            final Instant updatedAt,
            final boolean deleted
    ) {
        this.id = id;
        this.version = version;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deleted = deleted;
    }

    /**
     * Mapper converting this entity into a model
     *
     * @return Model built from this entity
     */
    public abstract M toModel();
}
