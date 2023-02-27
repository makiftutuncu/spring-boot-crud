package dev.akif.crud.simplest

import dev.akif.crud.CRUDRepository
import dev.akif.crud.CRUDService
import dev.akif.crud.common.InstantProvider
import java.io.Serializable

/**
 * The simplest variant of [CRUDService] where models are entities directly
 *
 * This is meant to be extended from a **@Service** class.
 *
 * @param I      Id type of the data
 * @param E      Entity type of the data which is a [SimplestEntity]
 * @param R      Repository type of the data which is a [SimplestRepository]
 * @param Mapper Mapper type of the data which is a [SimplestMapper]
 */
abstract class SimplestService<
        I : Serializable,
        E : SimplestEntity<I, E>,
        out R : SimplestRepository<I, E>,
        out Mapper : SimplestMapper<I, E>>(
    override val typeName: String,
    override val instantProvider: InstantProvider,
    crudRepository: CRUDRepository<I, E>,
    override val mapper: Mapper
) : CRUDService<I, E, E, E, E, R, Mapper>(typeName, instantProvider, crudRepository, mapper)
