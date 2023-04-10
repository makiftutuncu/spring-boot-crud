package dev.akif.crud

import dev.akif.crud.common.Paged
import dev.akif.crud.common.Parameters
import org.springframework.data.domain.PageRequest
import java.io.Serializable
import java.time.Instant

/**
 * Test data provider and mapper to convert between models and entities in tests
 *
 * @param I  Id type of the data
 * @param M  Model type of the data which is a [CRUDModel]
 * @param E  Entity type of the data which is a [CRUDEntity]
 * @param CM Create model type of the data which is a [CRUDCreateModel]
 * @param UM Update model type of the data which is a [CRUDUpdateModel]
 * @param TestData Test data type of the data which is a [CRUDTestData], meant to be the concrete type that extends this class
 *
 * @property typeName Type name of the data
 */
abstract class CRUDTestData<
        I : Serializable,
        E : CRUDEntity<I>,
        M : CRUDModel<I>,
        CM : CRUDCreateModel,
        UM : CRUDUpdateModel,
        TestData : CRUDTestData<I, E, M, CM, UM, TestData>>(val typeName: String) {
    /**
     * Instance of a [InMemoryCRUDRepository] containing test entities
     */
    abstract val repository: InMemoryCRUDRepository<I, E, CM, TestData>

    /**
     * Instance of an [IdGenerator] to use in tests
     */
    abstract val idGenerator: IdGenerator<I>

    /**
     * First test entity
     */
    abstract val testEntity1: E

    /**
     * Second test entity
     */
    abstract val testEntity2: E

    /**
     * Third test entity
     */
    abstract val testEntity3: E

    /**
     * More test entities if needed
     */
    abstract val moreTestEntities: Array<E>

    /**
     * Entities that are returned in the first page while listing with default pagination parameters
     */
    abstract val defaultFirstPageEntities: List<E>

    /**
     * [Pair]s of [PageRequest] and [Paged] as test cases for pagination
     * where the [Paged] is the expected result for the given [PageRequest]
     */
    abstract val paginationTestCases: List<Pair<PageRequest, Paged<E>>>

    /**
     * Default [Parameters] to use in tests
     */
    abstract val testParameters: Parameters

    /**
     * Copies an entity
     *
     * @param entity Entity to copy
     * @return Copied entity
     */
    abstract fun copy(entity: E): E

    /**
     * Check if two entities are duplicates, this should reflect your unique constraint on the DB
     *
     * @param e1 First entity to check
     * @param e2 Second entity to check
     * @return true if they are duplicates, false otherwise
     */
    abstract fun areDuplicates(e1: E, e2: E): Boolean

    /**
     * Mapper to convert from given entity to create model
     *
     * @param entity Entity to convert
     * @return Create model built from given entity
     */
    abstract fun entityToCreateModel(entity: E): CM

    /**
     * Mapper to convert from given entity to update model with no modifications
     *
     * @param entity Entity to convert
     * @return Update model built from given entity with no modifications
     */
    abstract fun entityToUpdateModelWithNoModifications(entity: E): UM

    /**
     * Mapper to convert from given entity to update model with some modifications
     *
     * @param entity Entity to convert
     * @return Update model built from given entity with some modifications
     */
    abstract fun entityToUpdateModelWithModifications(entity: E): UM

    /**
     * Instant provider to use in tests
     */
    val instantProvider: AdjustableInstantProvider =
        AdjustableInstantProvider()

    /**
     * Now instant to use in tests
     */
    fun now(): Instant =
        instantProvider.now()

    /**
     * Function to generate a random id
     *
     * @return Generated random id
     */
    fun randomId(): I =
        idGenerator.random()
}
