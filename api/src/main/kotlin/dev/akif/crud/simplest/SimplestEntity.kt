package dev.akif.crud.simplest

import dev.akif.crud.*
import java.io.Serializable
import java.time.Instant

/**
 * Simpler variant of [CRUDEntity] which is also a [CRUDModel], a [CRUDCreateModel],
 * a [CRUDUpdateModel], a [CRUDDTO], a [CRUDCreateDTO] and a [CRUDUpdateDTO]
 *
 * @param I Id type of the data
 * @param E Entity type which is also a [CRUDCreateModel], a [CRUDUpdateModel],
 * a [CRUDCreateDTO] and a [CRUDUpdateDTO], meant to be the exact type extending this class
 */
abstract class SimplestEntity<I : Serializable, E : CRUDEntity<I, E>>(
    override var id: I?,
    override var version: Int?,
    override var createdAt: Instant?,
    override var updatedAt: Instant?,
    override var deletedAt: Instant?
) : CRUDEntity<I, E>(
    id,
    version,
    createdAt,
    updatedAt,
    deletedAt
), CRUDModel<I>, CRUDCreateModel, CRUDUpdateModel, CRUDDTO<I>, CRUDCreateDTO, CRUDUpdateDTO {
    override fun id(): I {
        return id!!
    }

    override fun version(): Int {
        return version!!
    }

    override fun createdAt(): Instant {
        return createdAt!!
    }

    override fun updatedAt(): Instant {
        return updatedAt!!
    }

    override fun deletedAt(): Instant? {
        return deletedAt
    }
}
