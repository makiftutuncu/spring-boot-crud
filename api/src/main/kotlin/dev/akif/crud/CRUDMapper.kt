package dev.akif.crud

import java.io.Serializable
import java.time.Instant

/**
 * Mapper to convert between models and entities
 *
 * @param I  Id type of the data
 * @param E  Entity type of the data which is a [CRUDEntity]
 * @param M  Model type of the data which is a [CRUDModel]
 * @param CM Create model type of the data which is a [CRUDCreateModel]
 * @param UM Update model type of the data which is a [CRUDUpdateModel]
 */
interface CRUDMapper<
        I : Serializable,
        E : CRUDEntity<I, E>,
        out M : CRUDModel<I>,
        in CM : CRUDCreateModel,
        in UM : CRUDUpdateModel> {
    /**
     * Mapper to convert given model into an entity to be created
     *
     * @param createModel Create model to convert
     * @param now         Current instant
     * @return Entity to be created, built from this model
     */
    fun entityToBeCreatedFrom(createModel: CM, now: Instant): E

    /**
     * Mapper to convert from given entity to model
     *
     * @param entity Entity to convert
     * @return Model built from given entity
     */
    fun entityToModel(entity: E): M

    /**
     * Applies updates contained in given update model to given entity
     *
     * @param updateModel Update model to apply
     * @param entity      Entity to update
     * @return Updated entity
     */
    fun updateEntityWith(updateModel: UM, entity: E)
}
