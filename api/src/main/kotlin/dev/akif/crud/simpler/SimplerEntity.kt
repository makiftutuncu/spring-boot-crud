package dev.akif.crud.simpler

import dev.akif.crud.CRUDEntity
import java.io.Serializable

/**
 * Simpler variant of [CRUDEntity]
 *
 * @param I Id type of the data
 */
abstract class SimplerEntity<I : Serializable> : CRUDEntity<I>()
