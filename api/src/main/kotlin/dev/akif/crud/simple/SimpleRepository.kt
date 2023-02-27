package dev.akif.crud.simple

import dev.akif.crud.CRUDRepository
import java.io.Serializable

/**
 * Simple variant of [CRUDRepository]
 *
 * This is meant to be implemented from a **@Repository** interface.
 *
 * @param I Id type of the data
 * @param E Entity type of the data which is a [SimpleEntity]
 */
interface SimpleRepository<I : Serializable, E : SimpleEntity<I>> : CRUDRepository<I, E>
