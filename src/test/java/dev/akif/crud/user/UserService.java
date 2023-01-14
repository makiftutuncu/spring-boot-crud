package dev.akif.crud.user;

import dev.akif.crud.CRUDService;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.UUID;

@Service
public class UserService extends CRUDService<UUID, User, UserEntity, CreateUser, UpdateUser, UserRepository> {
    public UserService(final Clock clock, final UserRepository repository) {
        super("User", clock, repository);
    }
}
