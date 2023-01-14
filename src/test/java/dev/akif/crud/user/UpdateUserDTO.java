package dev.akif.crud.user;

import dev.akif.crud.BaseUpdateDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateUserDTO(
        @NotBlank String name,
        @NotNull LocalDate birthDate
) implements BaseUpdateDTO<UUID, User, UserEntity, UpdateUser> {
    @Override
    public UpdateUser toUpdateModel() {
        return new UpdateUser(name, birthDate);
    }
}
