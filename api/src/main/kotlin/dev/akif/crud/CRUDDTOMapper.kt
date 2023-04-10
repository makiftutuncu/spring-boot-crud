package dev.akif.crud

import dev.akif.crud.common.Parameters
import java.io.Serializable

/**
 * Mapper to convert between DTOs, models and entities
 *
 * @param I  Id type of the data
 * @param M  Model type of the data which is a [CRUDModel]
 * @param D  DTO type of the data which is a [CRUDDTO]
 * @param CM Create model type of the data which is a [CRUDCreateModel]
 * @param UM Update model type of the data which is a [CRUDUpdateModel]
 * @param CD Create DTO type of the data which is a [CRUDCreateDTO]
 * @param UD Update DTO type of the data which is a [CRUDUpdateDTO]
 */
interface CRUDDTOMapper<
        I : Serializable,
        in M : CRUDModel<I>,
        out D : CRUDDTO<I>,
        out CM : CRUDCreateModel,
        out UM : CRUDUpdateModel,
        in CD : CRUDCreateDTO,
        in UD : CRUDUpdateDTO> {
    /**
     * Mapper to convert from model to DTO
     *
     * @param model Model to convert
     * @param parameters [Parameters] to use in conversion
     * @return DTO built from given model
     */
    fun modelToDTO(model: M, parameters: Parameters): D

    /**
     * Mapper to convert given create DTO to a create model
     *
     * @param createDTO Create DTO to convert
     * @param parameters [Parameters] to use in conversion
     * @return Create model built from this create DTO
     */
    fun createDTOToCreateModel(createDTO: CD, parameters: Parameters): CM

    /**
     * Mapper to convert given update DTO to a update model
     *
     * @param updateDTO Update DTO to convert
     * @param parameters [Parameters] to use in conversion
     * @return Create model built from this update DTO
     */
    fun updateDTOToUpdateModel(updateDTO: UD, parameters: Parameters): UM
}
