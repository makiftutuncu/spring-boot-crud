package dev.akif.crud.user;

import dev.akif.crud.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "user")
@Getter
@Table(name = "users")
@Setter
@ToString(callSuper = true)
public class UserEntity extends BaseEntity<UUID, User> {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate birthDate;

    public UserEntity(
            final UUID id,
            final int version,
            final Instant createdAt,
            final Instant updatedAt,
            final boolean deleted,
            final String name,
            final LocalDate birthDate
    ) {
        super(id, version, createdAt, updatedAt, deleted);
        this.name = name;
        this.birthDate = birthDate;
    }

    public UserEntity(final String name, final LocalDate birthDate) {
        this(UUID.randomUUID(), 0, null, null, false, name, birthDate);
    }

    public UserEntity() {
        this(null, null);
    }

    @Override
    public User toModel() {
        return new User(getId(), getVersion(), getCreatedAt(), getUpdatedAt(), name, birthDate);
    }
}
