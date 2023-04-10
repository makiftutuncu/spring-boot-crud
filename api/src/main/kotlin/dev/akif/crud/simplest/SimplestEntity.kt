package dev.akif.crud.simplest

import dev.akif.crud.*
import java.io.Serializable
import java.time.Instant

/**
 * Simpler variant of [CRUDEntity] which is also a [CRUDModel], a [CRUDCreateModel],
 * a [CRUDUpdateModel], a [CRUDDTO], a [CRUDCreateDTO] and a [CRUDUpdateDTO]
 *
 * @param I Id type of the data
 */
abstract class SimplestEntity<I : Serializable> : CRUDEntity<I>(), CRUDModel<I>, CRUDCreateModel, CRUDUpdateModel, CRUDDTO<I>, CRUDCreateDTO, CRUDUpdateDTO {
    override fun id(): I = requireNotNull(id) { "id is required." }
    override fun version(): Int = requireNotNull(version) { "version is required." }
    override fun createdAt(): Instant = requireNotNull(createdAt) { "createdAt is required." }
    override fun updatedAt(): Instant = requireNotNull(updatedAt) { "updatedAt is required." }
    override fun deletedAt(): Instant? = deletedAt
}
