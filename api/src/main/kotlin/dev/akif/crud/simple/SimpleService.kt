package dev.akif.crud.simple

import dev.akif.crud.CRUDService
import dev.akif.crud.common.InstantProvider
import java.io.Serializable

/**
 * Simple variant of [CRUDService] where create/update models are just models directly
 *
 * This is meant to be extended from a **@Service** class.
 *
 * @param I      Id type of the data
 * @param E      Entity type of the data which is a [SimpleEntity]
 * @param M      Model type of the data which is a [SimpleModel]
 * @param D      DTO type of the data which is a [SimpleDTO]
 * @param Mapper Mapper type of the data which is a [SimpleMapper]
 */
abstract class SimpleService<
        I : Serializable,
        E : SimpleEntity<I, E>,
        M : SimpleModel<I>,
        D : SimpleDTO<I>,
        out Mapper : SimpleMapper<I, E, M, D>>(
    override val typeName: String,
    override val instantProvider: InstantProvider,
    override val repository: SimpleRepository<I, E>,
    override val mapper: Mapper
) : CRUDService<I, E, M, M, M, Mapper>(typeName, instantProvider, repository, mapper)
