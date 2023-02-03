package dev.akif.crud.simple

import dev.akif.crud.CRUDCreateModel
import dev.akif.crud.CRUDModel
import dev.akif.crud.CRUDUpdateModel
import java.io.Serializable

/**
 * Simple variant of [CRUDModel] which is also a [CRUDCreateModel] and a [CRUDUpdateModel]
 *
 * @param I Id type of the data
 */
interface SimpleModel<out I : Serializable> : CRUDModel<I>, CRUDCreateModel, CRUDUpdateModel
