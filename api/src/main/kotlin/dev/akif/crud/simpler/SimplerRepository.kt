package dev.akif.crud.simpler

import dev.akif.crud.CRUDRepository
import java.io.Serializable

/**
 * Simpler variant of [CRUDRepository]
 *
 * This is meant to be implemented from a **@Repository** interface.
 *
 * @param I Id type of the data
 * @param E Entity type of the data which is a [SimplerEntity]
 */
interface SimplerRepository<I : Serializable, E : SimplerEntity<I, E>> : CRUDRepository<I, E>
