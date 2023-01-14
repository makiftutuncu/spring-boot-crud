package dev.akif.crud.user;

import dev.akif.crud.BaseCreateModel;

import java.time.LocalDate;
import java.util.UUID;

public record CreateUser(String name, LocalDate birthDate) implements BaseCreateModel<UUID, User, UserEntity> {
    @Override
    public UserEntity toEntity() {
        return new UserEntity(name, birthDate);
    }
}
