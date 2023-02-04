package dev.akif.crud.simplest

import dev.akif.crud.CRUDController
import java.io.Serializable

/**
 * The simplest variant of [CRUDController] where the only data model is the entity
 *
 * This variant is only recommended for very simple data models.
 * Whenever possible, try and use one of the other variants.
 *
 * This is meant to be extended from a **@RestController** class,
 * ideally also with a **@RequestMapping** with some path prefix for the endpoints.
 *
 * @param I      Id type of the data
 * @param E      Entity type of the data which is a [SimplestEntity]
 * @param Mapper Mapper type of the data which is a [SimplestMapper]
 * @param S      Service type of the data which is a [SimplestService]
 */
abstract class SimplestController<
        I : Serializable,
        E : SimplestEntity<I, E>,
        out Mapper : SimplestMapper<I, E>,
        out S : SimplestService<I, E, Mapper>>(
    override val typeName: String,
    override val service: S,
    override val mapper: Mapper
) : CRUDController<I, E, E, E, E, E, E, E, Mapper, Mapper, S>(typeName, service, mapper)
