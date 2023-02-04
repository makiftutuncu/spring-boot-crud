package dev.akif.crud

import dev.akif.crud.error.CRUDError
import dev.akif.crud.error.CRUDErrorException
import org.junit.jupiter.api.*
import org.springframework.http.HttpStatus
import java.io.Serializable
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.function.BiPredicate

/**
 * Base class for unit tests of CRUD services
 *
 * @param I          Id type of the data
 * @param M          Model type of the data which is a [CRUDModel]
 * @param E          Entity type of the data which is a [CRUDEntity]
 * @param CM         Create model type of the data which is a [CRUDCreateModel]
 * @param UM         Update model type of the data which is a [CRUDUpdateModel]
 * @param Mapper     Mapper type of the data which is a [CRUDMapper]
 * @param TestMapper Test mapper type of the data which is a [CRUDTestMapper]
 * @param S          Service type of the data which is a [CRUDService]
 *
 * @property typeName         Type name of the data
 * @property mapper           Mapper dependency of this test which is a [CRUDMapper]
 * @property testMapper       Test mapper dependency of this test which is a [CRUDTestMapper]
 * @property duplicateCheck   [BiPredicate] to check if two entities are duplicates
 * @property testEntity1      First test entity
 * @property testEntity2      Second test entity
 * @property testEntity3      Third test entity
 * @property moreTestEntities More test entities if needed
 */
abstract class CRUDServiceTest<
        I : Serializable,
        M : CRUDModel<I>,
        E : CRUDEntity<I, E>,
        CM : CRUDCreateModel,
        UM : CRUDUpdateModel,
        Mapper : CRUDMapper<I, E, M, CM, UM>,
        TestMapper : CRUDTestMapper<I, E, M, CM, UM>,
        S : CRUDService<I, M, E, CM, UM, Mapper>>(
    protected val typeName: String,
    protected val mapper: Mapper,
    protected val testMapper: TestMapper,
    protected val duplicateCheck: BiPredicate<E, E>,
    protected val testEntity1: E,
    protected val testEntity2: E,
    protected val testEntity3: E,
    protected vararg val moreTestEntities: E
) {
    protected lateinit var repository: InMemoryCRUDRepository<I, E, CM, TestMapper>

    protected lateinit var service: S

    /**
     * Build a concrete instance of the service to be tested using given test dependencies
     *
     * @param clock      Fixed test clock
     * @param repository [InMemoryCRUDRepository] built with test entities
     * @param mapper     Concrete [Mapper] instance as a dependency
     * @return Concrete instance of the service to be tested, built using given test dependencies
     */
    protected abstract fun buildService(clock: Clock, repository: CRUDRepository<I, E>, mapper: Mapper): S

    /** @suppress */
    @BeforeEach
    fun setUp() {
        repository = InMemoryCRUDRepository(
            typeName,
            testMapper,
            duplicateCheck,
            testEntity1,
            testEntity2,
            testEntity3,
            *moreTestEntities
        )
        service = buildService(clock, repository, mapper)
    }

    /** @suppress */
    @Nested
    inner class Creating {
        /** @suppress */
        @DisplayName("should fail with already exists error when trying to create an entity that already exists")
        @Test
        fun testCreateAlreadyExists() {
            val createModel = testMapper.entityToCreateModel(testEntity1)
            val actual = Assertions.assertThrows(
                CRUDErrorException::class.java
            ) { service.create(createModel) }.error
            val expected = alreadyExistsError(createModel)
            Assertions.assertEquals(expected, actual)
        }

        /** @suppress */
        @DisplayName("should successfully create a new entity")
        @Test
        fun testCreate() {
            repository.clear()
            val createModel = testMapper.entityToCreateModel(testEntity1)
            val actual = service.create(createModel)
            val expected = testMapper.entityToModelWithExpectedId(testEntity1, actual.id())
            Assertions.assertEquals(expected, actual)
            val foundEntity = repository.findByIdAndDeletedAtIsNull(actual.id())
                ?.let { testMapper.entityToModelWithExpectedId(it, actual.id()) }
            Assertions.assertEquals(expected, foundEntity)
        }
    }

    private fun alreadyExistsError(createModel: CM): CRUDError {
        return CRUDError(HttpStatus.CONFLICT, "$typeName with $createModel already exists.")
    }

    /** @suppress */
    companion object {
        /**
         * Fixed [Instant] for tests
         */
        @JvmStatic
        protected val now: Instant = Instant.now()

        /**
         * Fixed [Clock] for tests
         */
        @JvmStatic
        protected val clock: Clock = Clock.fixed(now, ZoneOffset.UTC)

        /**
         * Fixed [LocalDate] for tests
         */
        @JvmStatic
        protected val today: LocalDate = LocalDate.now(clock)
    }
}
