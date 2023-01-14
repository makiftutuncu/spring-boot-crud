package dev.akif.crud.user;

import dev.akif.crud.CRUDRepository;
import org.springframework.data.repository.Repository;

import java.util.UUID;

@org.springframework.stereotype.Repository
public interface UserRepository extends CRUDRepository<UUID, User, UserEntity>, Repository<UserEntity, UUID> {
}
