package dev.akif.crud.simpler

import dev.akif.crud.*
import java.io.Serializable

/**
 * Simpler variant of [CRUDModel] which is also a [CRUDCreateModel],
 * a [CRUDUpdateModel], a [CRUDDTO], a [CRUDCreateDTO] and a [CRUDUpdateDTO]
 *
 * @param I Id type of the data
 */
interface SimplerModel<out I : Serializable> : CRUDModel<I>, CRUDCreateModel, CRUDUpdateModel, CRUDDTO<I>,
    CRUDCreateDTO, CRUDUpdateDTO
