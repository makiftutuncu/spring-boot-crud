package dev.akif.crud.simpler

import dev.akif.crud.CRUDDTOMapper
import dev.akif.crud.CRUDMapper
import dev.akif.crud.common.Parameters
import java.io.Serializable

/**
 * Simpler variant of [CRUDMapper] where create/update models and DTOs are just models,
 * which is also a [CRUDDTOMapper]
 *
 * @param I Id type of the entity
 * @param E Entity type of the data which is a [SimplerEntity]
 * @param M Model type of the data which is a [SimplerModel]
 */
interface SimplerMapper<
        I : Serializable,
        E : SimplerEntity<I>,
        M : SimplerModel<I>> : CRUDMapper<I, E, M, M, M>, CRUDDTOMapper<I, M, M, M, M, M, M> {
    override fun modelToDTO(model: M, parameters: Parameters): M = model

    override fun createDTOToCreateModel(createDTO: M, parameters: Parameters): M = createDTO

    override fun updateDTOToUpdateModel(updateDTO: M, parameters: Parameters): M = updateDTO
}
