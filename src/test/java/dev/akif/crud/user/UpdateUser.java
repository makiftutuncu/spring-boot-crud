package dev.akif.crud.user;

import dev.akif.crud.BaseUpdateModel;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateUser(String name, LocalDate birthDate) implements BaseUpdateModel<UUID, User, UserEntity> {
    @Override
    public void applyUpdatesTo(final UserEntity entity) {
        entity.setName(name);
        entity.setBirthDate(birthDate);
    }
}
