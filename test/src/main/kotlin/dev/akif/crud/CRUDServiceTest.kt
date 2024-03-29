package dev.akif.crud

import dev.akif.crud.CRUDTestData.*
import dev.akif.crud.common.Paged
import dev.akif.crud.error.CRUDError
import dev.akif.crud.error.CRUDErrorException
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import java.io.Serializable

/**
 * Base class for unit tests of CRUD services
 *
 * @param I        Id type of the data
 * @param E        Entity type of the data which is a [CRUDEntity]
 * @param M        Model type of the data which is a [CRUDModel]
 * @param CM       Create model type of the data which is a [CRUDCreateModel]
 * @param UM       Update model type of the data which is a [CRUDUpdateModel]
 * @param Mapper   Mapper type of the data which is a [CRUDMapper]
 * @param R        Repository type of the data which is a [CRUDRepository]
 * @param S        Service type of the data which is a [CRUDService]
 * @param TestData Test data type of the data which is a [CRUDTestData]
 *
 * @property mapper       Mapper dependency of this test which is a [CRUDMapper]
 * @property testData     Test data dependency of this test which is a [CRUDTestData]
 * @property buildService Function to build a concrete instance of the service to be tested using given test dependencies
 */
abstract class CRUDServiceTest<
        I : Serializable,
        E : CRUDEntity<I>,
        M : CRUDModel<I>,
        CM : CRUDCreateModel,
        UM : CRUDUpdateModel,
        Mapper : CRUDMapper<I, E, M, CM, UM>,
        R : CRUDRepository<I, E>,
        S : CRUDService<I, E, M, CM, UM, R, Mapper>,
        TestData : CRUDTestData<I, E, M, CM, UM, TestData>>(
    protected val mapper: Mapper,
    protected val testData: TestData,
    protected val buildService: (Mapper, TestData) -> S
) {
    /**
     * Instance of the service to be tested, this will be reset for each test method.
     *
     * @see [BeforeEach]
     */
    protected lateinit var service: S

    /**
     * Resets test data, called before each test method.
     */
    protected abstract fun resetData()

    /** @suppress */
    @BeforeEach
    fun setUp() {
        resetData()
        testData.instantProvider.reset()
        service = buildService(mapper, testData)
    }

    /** @suppress */
    @DisplayName("creating new entities")
    @Nested
    inner class Creating {
        /** @suppress */
        @DisplayName("should fail with already exists error when trying to create an entity that already exists")
        @Test
        fun testCreateAlreadyExists() {
            val createModel = testData.entityToCreateModel(testData.testEntity1)
            val exception = assertThrows(CRUDErrorException::class.java) { service.create(createModel, testData.testParameters) }

            val actual = exception.error
            val expected = CRUDError(HttpStatus.CONFLICT, "${testData.typeName} with $createModel already exists.")

            assertEquals(expected, actual)
        }

        /** @suppress */
        @DisplayName("should create a new entity and return it")
        @Test
        fun testCreate() {
            testData.repository.clear()
            val createModel = testData.entityToCreateModel(testData.testEntity1)

            val actual = service.create(createModel, testData.testParameters)
            val expected = mapper.entityToModel(testData.copy(testData.testEntity1).apply { id = actual.id() })

            assertEquals(expected, actual)

            val found = service.get(actual.id(), testData.testParameters)

            assertEquals(expected, found)
        }

        /** @suppress */
        @DisplayName("should create a new entity with the same data of a deleted entity and return it")
        @Test
        fun testCreateAgain() {
            testData.repository.update(testData.copy(testData.testEntity1).apply { deletedAt = testData.now() })
            val createModel = testData.entityToCreateModel(testData.testEntity1)

            val actual = service.create(createModel, testData.testParameters)
            val expected = mapper.entityToModel(testData.copy(testData.testEntity1).apply { id = actual.id() })

            assertEquals(expected, actual)

            val found = service.get(actual.id(), testData.testParameters)

            assertEquals(expected, found)
            assertNotEquals(testData.testEntity1, found)
        }
    }

    /** @suppress */
    @DisplayName("listing entities")
    @Nested
    inner class Listing {
        @DisplayName("should return some entities with default pagination")
        @Test
        fun testListWithDefaultPagination() {
            val actual = service.list(PageRequest.of(0, 20), testData.testParameters)
            val expected = Paged(
                data = testData.defaultFirstPageEntities.map(mapper::entityToModel),
                page = 0,
                perPage = 20,
                totalPages = 1
            )

            assertEquals(expected, actual)
        }

        @DisplayName("should return correct entities with pagination")
        @Test
        fun testListWithPagination() {
            testData.paginationTestCases.forEach { (pageRequest, pagedEntities) ->
                val expected = pagedEntities.map(mapper::entityToModel)
                val actual = service.list(pageRequest, testData.testParameters)

                assertEquals(expected, actual)
            }
        }

        @DisplayName("should return empty page when no entities exist")
        @Test
        fun testListWithNoEntities() {
            testData.repository.clear()

            val actual = service.list(PageRequest.of(0, 20), testData.testParameters)
            val expected = Paged.empty<M>(page = 0, perPage = 20, totalPages = 0)

            assertEquals(expected, actual)
        }

        @DisplayName("should not return deleted entities")
        @Test
        fun testListWithNoDeletedEntities() {
            testData.repository.update(testData.copy(testData.testEntity3).apply { deletedAt = testData.now() })
            val testCases: List<Pair<PageRequest, Paged<M>>> = listOf(
                PageRequest.of(0, 1) to Paged(
                    data = listOf(
                        mapper.entityToModel(testData.testEntity1)
                    ),
                    page = 0,
                    perPage = 1,
                    totalPages = 2
                ),
                PageRequest.of(1, 1) to Paged(
                    data = listOf(
                        mapper.entityToModel(testData.testEntity2)
                    ),
                    page = 1,
                    perPage = 1,
                    totalPages = 2
                )
            )

            for ((pageable, expected) in testCases) {
                val actual = service.list(pageable, testData.testParameters)

                assertEquals(expected, actual)
            }
        }
    }

    /** @suppress */
    @DisplayName("getting an entity")
    @Nested
    inner class Getting {
        @DisplayName("should return null when trying to get an entity that doesn't exist")
        @Test
        fun testGetNotFound() {
            assertNull(service.get(testData.randomId(), testData.testParameters))
        }

        @DisplayName("should return correct entity")
        @Test
        fun testGet() {
            val actual = service.get(testData.testEntity1.id!!, testData.testParameters)
            val expected = mapper.entityToModel(testData.testEntity1)

            assertEquals(expected, actual)
        }

        @DisplayName("should return null when trying to get a deleted entity")
        @Test
        fun testGetDeletedNotFound() {
            testData.repository.update(testData.copy(testData.testEntity1).apply { deletedAt = testData.now() })
            assertNull(service.get(testData.testEntity1.id!!, testData.testParameters))
        }
    }

    /** @suppress */
    @DisplayName("updating an entity")
    @Nested
    inner class Updating {
        @DisplayName("should fail with not found error when trying to update an entity that doesn't exist")
        @Test
        fun testUpdateNotFound() {
            val id = testData.randomId()
            val updateModel = testData.entityToUpdateModelWithModifications(testData.testEntity1)
            val exception = assertThrows(CRUDErrorException::class.java) { service.update(id, updateModel, testData.testParameters) }

            val actual = exception.error
            val expected = CRUDError(HttpStatus.NOT_FOUND, "${testData.typeName} with id $id is not found.")

            assertEquals(expected, actual)
        }

        @DisplayName("should fail with already exists error when trying to update an entity as duplicate of another entity")
        @Test
        fun testUpdateAlreadyExists() {
            testData.repository.entities[testData.testEntity2.id!!] = testData.copy(testData.testEntity1).apply { id = testData.testEntity2.id }
            val updateModel = testData.entityToUpdateModelWithNoModifications(testData.testEntity1)
            val exception =
                assertThrows(CRUDErrorException::class.java) { service.update(testData.testEntity2.id!!, updateModel, testData.testParameters) }

            val actual = exception.error
            val expected = CRUDError(HttpStatus.CONFLICT, "${testData.typeName} with $updateModel already exists.")

            assertEquals(expected, actual)
        }

        @DisplayName("should update return updated entity")
        @Test
        fun testUpdate() {
            val updateModel = testData.entityToUpdateModelWithModifications(testData.testEntity1)
            val oneSecondLater = testData.now().plusSeconds(1)
            testData.instantProvider.adjust { it.plusSeconds(1) }

            val actual = service.update(testData.testEntity1.id!!, updateModel, testData.testParameters)
            val expected = mapper.entityToModel(
                testData.copy(testData.testEntity1).apply {
                    mapper.updateEntityWith(this, updateModel)
                    version = version?.plus(1)
                    updatedAt = oneSecondLater
                }
            )

            assertEquals(expected, actual)

            val found = service.get(actual.id(), testData.testParameters)

            assertEquals(expected, found)
        }

        @DisplayName("should update with the same data of a deleted entity return updated entity")
        @Test
        fun testUpdateDeleted() {
            testData.repository.update(testData.copy(testData.testEntity2).apply { deletedAt = testData.now() })
            val updateModel = testData.entityToUpdateModelWithNoModifications(testData.testEntity2)
            val oneSecondLater = testData.now().plusSeconds(1)
            testData.instantProvider.adjust { it.plusSeconds(1) }

            val actual = service.update(testData.testEntity1.id!!, updateModel, testData.testParameters)
            val expected = mapper.entityToModel(
                testData.copy(testData.testEntity1).apply {
                    mapper.updateEntityWith(this, updateModel)
                    version = version?.plus(1)
                    updatedAt = oneSecondLater
                }
            )

            assertEquals(expected, actual)

            val found = service.get(actual.id(), testData.testParameters)

            assertEquals(expected, found)
        }
    }

    /** @suppress */
    @DisplayName("deleting an entity")
    @Nested
    inner class Deleting {
        @DisplayName("should fail with not found error when trying to delete an entity that doesn't exist")
        @Test
        fun testDeleteNotFound() {
            val id = testData.randomId()
            val exception = assertThrows(CRUDErrorException::class.java) { service.delete(id, testData.testParameters) }

            val actual = exception.error
            val expected = CRUDError(HttpStatus.NOT_FOUND, "${testData.typeName} with id $id is not found.")

            assertEquals(expected, actual)
        }

        @DisplayName("should delete")
        @Test
        fun testDelete() {
            val oneSecondLater = testData.now().plusSeconds(1)
            testData.instantProvider.adjust { it.plusSeconds(1) }

            service.delete(testData.testEntity1.id!!, testData.testParameters)

            assertNull(service.get(testData.testEntity1.id!!, testData.testParameters))

            val actual = testData.repository.entities[testData.testEntity1.id!!]?.let(mapper::entityToModel)
            val expected = mapper.entityToModel(
                testData.copy(testData.testEntity1).apply {
                    version = version?.plus(1)
                    updatedAt = oneSecondLater
                    deletedAt = oneSecondLater
                }
            )

            assertEquals(expected, actual)
        }
    }
}
