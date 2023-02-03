package dev.akif.crud.simple

import dev.akif.crud.CRUDService
import java.io.Serializable
import java.time.Clock

/**
 * Simple variant of [CRUDService] where create/update models are just models directly
 *
 * This is meant to be extended from a **@Service** class.
 *
 * @param I      Id type of the data
 * @param M      Model type of the data which is a [SimpleModel]
 * @param E      Entity type of the data which is a [SimpleEntity]
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
    override val clock: Clock,
    override val repository: SimpleRepository<I, E>,
    override val mapper: Mapper
) : CRUDService<I, M, E, M, M, Mapper>(typeName, clock, repository, mapper)
