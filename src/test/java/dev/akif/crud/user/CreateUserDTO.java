package dev.akif.crud.user;

import dev.akif.crud.BaseCreateDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record CreateUserDTO(
        @NotBlank String name,
        @NotNull LocalDate birthDate
) implements BaseCreateDTO<UUID, User, UserEntity, CreateUser> {
    @Override
    public CreateUser toCreateModel() {
        return new CreateUser(name, birthDate);
    }
}
