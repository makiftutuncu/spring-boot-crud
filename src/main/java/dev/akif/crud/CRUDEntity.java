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

@EntityListeners(AuditingEntityListener.class)
@Getter
@MappedSuperclass
@Setter
@ToString
public abstract class CRUDEntity<
        I extends Serializable,
        M extends CRUDModel<I>> {
    @Id
    protected I id;

    @Version
    protected int version;

    @CreatedDate
    protected Instant createdAt;

    @LastModifiedDate
    protected Instant updatedAt;

    protected boolean deleted;

    protected CRUDEntity(
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

    public abstract M toModel();
}
