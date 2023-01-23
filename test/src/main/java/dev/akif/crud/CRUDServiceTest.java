package dev.akif.crud;

import dev.akif.crud.error.CRUDError;
import dev.akif.crud.error.CRUDErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Base class for unit tests of CRUD services
 *
 * @param <I>          Id type of the data
 * @param <M>          Model type of the data
 * @param <E>          Entity type of the data
 * @param <CM>         Create model type of the data
 * @param <UM>         Update model type of the data
 * @param <Mapper>     Mapper type of the data
 * @param <TestMapper> Test mapper type of the data
 * @param <S>          Service type of the data
 */
public abstract class CRUDServiceTest<
        I extends Serializable,
        M extends CRUDModel<I>,
        E extends CRUDEntity<I, E>,
        CM extends CRUDCreateModel<I, E>,
        UM extends CRUDUpdateModel<I, E>,
        Mapper extends CRUDMapper<I, E, M, CM, UM>,
        TestMapper extends CRUDTestMapper<I, E, M, CM, UM>,
        S extends CRUDService<I, M, E, CM, UM, Mapper>> {
    /**
     * Type name of the data this service manages
     */
    protected final String type;
    /**
     * {@link BiPredicate} to check if two entities are duplicates
     */
    protected final BiPredicate<E, E> duplicateCheck;
    /**
     * Test entity 1 for tests
     */
    protected final E testEntity1;
    /**
     * Test entity 2 for tests
     */
    protected final E testEntity2;
    /**
     * Test entity 3 for tests
     */
    protected final E testEntity3;
    /**
     * More test entities for tests, if needed
     */
    protected final E[] moreTestEntities;

    /**
     * Fixed {@link Instant} for tests
     */
    protected static final Instant now = Instant.now();
    /**
     * Fixed {@link Clock} for tests
     */
    protected static final Clock clock = Clock.fixed(now, ZoneOffset.UTC);
    /**
     * Fixed {@link LocalDate} for tests
     */
    protected static final LocalDate today = LocalDate.now(clock);

    /**
     * {@link InMemoryCRUDRepository} built with test entities, will be reset before every test case
     */
    protected InMemoryCRUDRepository<I, E, CM, TestMapper> repository;
    /**
     * {@link Mapper} dependency of the service to be tested
     */
    protected Mapper mapper;
    /**
     * {@link TestMapper} to be used in test assertions
     */
    protected TestMapper testMapper;
    /**
     * {@link CRUDService} to be tested
     */
    protected S service;

    /**
     * Constructor to provide test entities and dependencies to this test
     *
     * @param type             Type name of the data this service manages
     * @param mapper           {@link Mapper} dependency of the service to be tested
     * @param testMapper       {@link TestMapper} to be used in test assertions
     * @param duplicateCheck   {@link BiPredicate} to check if two entities are duplicates
     * @param testEntity1      Test entity 1
     * @param testEntity2      Test entity 2
     * @param testEntity3      Test entity 3
     * @param moreTestEntities More test entities, if needed
     */
    @SafeVarargs
    protected CRUDServiceTest(
            final String type,
            final Mapper mapper,
            final TestMapper testMapper,
            final BiPredicate<E, E> duplicateCheck,
            final E testEntity1,
            final E testEntity2,
            final E testEntity3,
            final E... moreTestEntities
    ) {
        this.type = type;
        this.mapper = mapper;
        this.testMapper = testMapper;
        this.testEntity1 = testEntity1;
        this.testEntity2 = testEntity2;
        this.testEntity3 = testEntity3;
        this.moreTestEntities = moreTestEntities;
        this.duplicateCheck = duplicateCheck;
    }

    /**
     * Build a concrete instance of the service to be tested using given test dependencies
     *
     * @param clock      Fixed test clock
     * @param repository {@link InMemoryCRUDRepository} built with test entities
     * @param mapper     Concrete {@link Mapper} instance as a dependency
     * @return Concrete instance of the service to be tested, built using given test dependencies
     */
    protected abstract S buildService(final Clock clock, final CRUDRepository<I, E> repository, final Mapper mapper);

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        final var entities = (E[]) Array.newInstance(testEntity1.getClass(), moreTestEntities.length + 3);
        entities[0] = testEntity1;
        entities[1] = testEntity2;
        entities[2] = testEntity3;
        System.arraycopy(moreTestEntities, 0, entities, 3, moreTestEntities.length);

        repository = new InMemoryCRUDRepository<>(type, testMapper, duplicateCheck, entities);
        service = buildService(clock, repository, mapper);
    }

    @Nested
    class Creating {
        @DisplayName("should fail with already exists error when trying to create an entity that already exists")
        @Test
        void testCreateAlreadyExists() {
            final var createModel = testMapper.entityToCreateModel(testEntity1);
            final var actual = assertThrows(CRUDErrorException.class, () -> service.create(createModel)).getError();
            final var expected = alreadyExistsError(createModel);
            assertEquals(expected, actual);
        }

        @DisplayName("should successfully create a new entity")
        @Test
        void testCreate() {
            repository.clear();
            final var createModel = testMapper.entityToCreateModel(testEntity1);
            final var actual = service.create(createModel);
            final var expected = testMapper.entityToModelWithExpectedId(testEntity1, actual.id());
            assertEquals(expected, actual);

            final var foundEntity = repository.findByIdAndDeletedAtIsNull(actual.id());
            assertEquals(expected, testMapper.entityToModelWithExpectedId(foundEntity.orElse(null), actual.id()));
        }
    }

    private CRUDError alreadyExistsError(final CM createModel) {
        return new CRUDError(HttpStatus.CONFLICT, "%s with %s already exists.".formatted(type, createModel));
    }
}
