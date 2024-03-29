package dev.akif.crud.simplest

import dev.akif.crud.CRUDDTOMapper
import dev.akif.crud.CRUDMapper
import dev.akif.crud.common.Parameters
import java.io.Serializable
import java.time.Instant

/**
 * The simplest variant of [CRUDMapper] where the only data model is the entity,
 * which is also a [CRUDDTOMapper]
 *
 * @param I Id type of the data
 * @param E Entity type of the data which is a [SimplestEntity]
 */
interface SimplestMapper<I : Serializable, E : SimplestEntity<I>> : CRUDMapper<I, E, E, E, E>,
    CRUDDTOMapper<I, E, E, E, E, E, E> {
    override fun modelToDTO(model: E, parameters: Parameters): E = model

    override fun entityToModel(entity: E): E = entity

    override fun createDTOToCreateModel(createDTO: E, parameters: Parameters): E = createDTO

    override fun updateDTOToUpdateModel(updateDTO: E, parameters: Parameters): E = updateDTO

    override fun entityToBeCreatedFrom(createModel: E, now: Instant): E = createModel
}
