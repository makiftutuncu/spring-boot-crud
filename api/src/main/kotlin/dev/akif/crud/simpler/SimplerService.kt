package dev.akif.crud.simpler

import dev.akif.crud.CRUDService
import java.io.Serializable
import java.time.Clock

/**
 * Simpler variant of [CRUDService] where create/update models are just models
 *
 * This is meant to be extended from a **@Service** class.
 *
 * @param I      Id type of the data
 * @param M      Model type of the data which is a [SimplerModel]
 * @param E      Entity type of the data which is a [SimplerEntity]
 * @param Mapper Mapper of the data which is a [SimplerMapper]
 */
abstract class SimplerService<
        I : Serializable,
        E : SimplerEntity<I, E>,
        M : SimplerModel<I>,
        out Mapper : SimplerMapper<I, E, M>>(
    override val typeName: String,
    override val clock: Clock,
    override val repository: SimplerRepository<I, E>,
    override val mapper: Mapper
) : CRUDService<I, E, M, M, M, Mapper>(typeName, clock, repository, mapper)
