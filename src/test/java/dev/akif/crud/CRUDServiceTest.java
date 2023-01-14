package dev.akif.crud;

import dev.akif.crud.user.*;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("CRUDService")
class CRUDServiceTest {
    public static final Instant NOW = Instant.now();
    private static final Clock clock = Clock.fixed(Instant.EPOCH, ZoneOffset.UTC);

    private UserRepository repository;
    private UserService service;

    @BeforeEach
    void setUp() {
        repository = mock(UserRepository.class);
        doNothing().when(repository).flush();

        service = new UserService(clock, repository);
    }

    @AfterEach
    void reset() {
        Mockito.reset(repository);
    }

    @DisplayName("creating entities")
    @Nested
    class CreatingEntities {
        @Test
        @DisplayName("should create an entity")
        void shouldCreateAnEntity() {
            final var id = UUID.randomUUID();
            final var name = "Mehmet Akif Tutuncu";
            final var birthDate = LocalDate.of(1991, 3, 23);
            final var createUser = new CreateUser(name, birthDate);
            final var userEntity = new UserEntity(id, 0, NOW, NOW, false, name, birthDate);

            when(repository.save(any(UserEntity.class))).thenReturn(userEntity);

            final var expected = new User(id, 0, NOW, NOW, name, birthDate);

            final var actual = service.create(createUser);

            assertEquals(expected, actual);
            verify(repository, times(1)).save(any(UserEntity.class));
        }
    }
}
