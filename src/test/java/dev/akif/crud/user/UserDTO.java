package dev.akif.crud.user;

import dev.akif.crud.BaseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record UserDTO(
        @NotNull UUID id,
        @NotNull Instant createdAt,
        @NotNull Instant updatedAt,
        @NotBlank String name,
        @NotNull LocalDate birthDate
) implements BaseDTO<UUID> {
}
