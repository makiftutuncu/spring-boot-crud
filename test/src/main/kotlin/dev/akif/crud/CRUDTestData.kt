package dev.akif.crud

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
 * @param typeName Type name of the data
 */
abstract class CRUDTestData<
        I : Serializable,
        E : CRUDEntity<I>,
        M : CRUDModel<I>,
        CM : CRUDCreateModel,
        UM : CRUDUpdateModel,
        TestData : CRUDTestData<I, E, M, CM, UM, TestData>>(typeName: String) {
    /**
     * Instance of a [InMemoryCRUDRepository] containing test entities
     */
    @Suppress("UNCHECKED_CAST")
    val repository: InMemoryCRUDRepository<I, E, CM, TestData> by lazy {
        InMemoryCRUDRepository(typeName, this as TestData)
    }

    /**
     * Instant provider to use in tests
     */
    val instantProvider: AdjustableInstantProvider = AdjustableInstantProvider()

    /**
     * Now instant to use in tests
     */
    fun now(): Instant = instantProvider.now()

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
     * Copies an entity
     *
     * @param entity Entity to copy
     * @return Copied entity
     */
    abstract fun copy(entity: E): E

    /**
     * Function to generate a random id
     *
     * @return Generated random id
     */
    abstract fun randomId(): I

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
}
