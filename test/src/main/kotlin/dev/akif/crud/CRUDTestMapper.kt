package dev.akif.crud

import java.io.Serializable

/**
 * Mapper to convert between models and entities in tests
 *
 * @param I          Id type of the data
 * @param M          Model type of the data which is a [CRUDModel]
 * @param E          Entity type of the data which is a [CRUDEntity]
 * @param CM         Create model type of the data which is a [CRUDCreateModel]
 * @param UM         Update model type of the data which is a [CRUDUpdateModel]
 */
interface CRUDTestMapper<I : Serializable, E : CRUDEntity<I, E>, M : CRUDModel<I>, CM : CRUDCreateModel, UM : CRUDUpdateModel> {
    /**
     * Mapper to convert from given entity to create model
     *
     * @param entity Entity to convert
     * @return Create model built from given entity
     */
    fun entityToCreateModel(entity: E): CM

    /**
     * Mapper to convert from given entity to model replacing its id with given expected id
     *
     * @param entity     Entity to convert
     * @param expectedId Expected id to with which to replace actual id
     * @return Model built from given entity with expected id
     */
    fun entityToModelWithExpectedId(entity: E, expectedId: I): M
}
