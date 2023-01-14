package dev.akif.crud.user;

import dev.akif.crud.CRUDController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequestMapping("/v1/users")
@RestController
@Tag(name = "Users", description = "CRUD operations for user entities")
public class UserController extends CRUDController<
        UUID,
        UserDTO,
        User,
        UserEntity,
        CreateUser,
        UpdateUser,
        CreateUserDTO,
        UpdateUserDTO,
        UserRepository,
        UserService> {
    public UserController(final UserService service) {
        super("User", service);
    }

    @Override
    public UserDTO toDTO(final User model) {
        return new UserDTO(
                model.id(),
                model.createdAt(),
                model.updatedAt(),
                model.name(),
                model.birthDate()
        );
    }
}
