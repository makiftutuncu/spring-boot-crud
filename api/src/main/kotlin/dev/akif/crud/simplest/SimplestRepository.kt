package dev.akif.crud.simplest

import dev.akif.crud.CRUDRepository
import java.io.Serializable

/**
 * The simplest variant of [CRUDRepository]
 *
 *
 * This is meant to be implemented from a **@Repository** interface.
 *
 * @param I Id type of the data
 * @param E Entity type of the data which is a [SimplestEntity]
 */
interface SimplestRepository<I : Serializable, E : SimplestEntity<I>> : CRUDRepository<I, E>
