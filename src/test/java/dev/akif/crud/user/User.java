package dev.akif.crud.user;

import dev.akif.crud.BaseModel;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record User(
        UUID id,
        int version,
        Instant createdAt,
        Instant updatedAt,
        String name,
        LocalDate birthDate
) implements BaseModel<UUID> {
}
