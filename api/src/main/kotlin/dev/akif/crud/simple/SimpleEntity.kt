package dev.akif.crud.simple

import dev.akif.crud.CRUDEntity
import java.io.Serializable

/**
 * Simple variant of [CRUDEntity]
 *
 * @param I Id type of the data
 */
abstract class SimpleEntity<I : Serializable> : CRUDEntity<I>()
