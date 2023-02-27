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
abstract class SimplestEntity<I : Serializable>(
    override var id: I? = null,
    override var version: Int? = null,
    override var createdAt: Instant? = null,
    override var updatedAt: Instant? = null,
    override var deletedAt: Instant? = null
) : CRUDEntity<I>(
    id,
    version,
    createdAt,
    updatedAt,
    deletedAt
), CRUDModel<I>, CRUDCreateModel, CRUDUpdateModel, CRUDDTO<I>, CRUDCreateDTO, CRUDUpdateDTO {
    override fun id(): I = requireNotNull(id) { "id was null." }
    override fun version(): Int = requireNotNull(version) { "version was null." }
    override fun createdAt(): Instant = requireNotNull(createdAt) { "createdAt was null." }
    override fun updatedAt(): Instant = requireNotNull(updatedAt) { "updatedAt was null." }
    override fun deletedAt(): Instant? = deletedAt
}
