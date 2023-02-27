package dev.akif.crud.simplest

import dev.akif.crud.CRUDDTOMapper
import dev.akif.crud.CRUDMapper
import java.io.Serializable

/**
 * The simplest variant of [CRUDMapper] where the only data model is the entity,
 * which is also a [CRUDDTOMapper]
 *
 * @param I Id type of the data
 * @param E Entity type of the data which is a [SimplestEntity]
 */
interface SimplestMapper<I : Serializable, E : SimplestEntity<I>> : CRUDMapper<I, E, E, E, E>,
    CRUDDTOMapper<I, E, E, E, E, E, E> {
    override fun modelToDTO(model: E): E = model

    override fun entityToModel(entity: E): E = entity
}
