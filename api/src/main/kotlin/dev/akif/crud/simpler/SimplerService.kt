package dev.akif.crud.simpler

import dev.akif.crud.CRUDService
import dev.akif.crud.common.InstantProvider
import java.io.Serializable

/**
 * Simpler variant of [CRUDService] where create/update models are just models
 *
 * This is meant to be extended from a **@Service** class.
 *
 * @param I      Id type of the data
 * @param E      Entity type of the data which is a [SimplerEntity]
 * @param M      Model type of the data which is a [SimplerModel]
 * @param R      Repository type of the data which is a [SimplerRepository]
 * @param Mapper Mapper of the data which is a [SimplerMapper]
 */
abstract class SimplerService<
        I : Serializable,
        E : SimplerEntity<I>,
        M : SimplerModel<I>,
        out R : SimplerRepository<I, E>,
        out Mapper : SimplerMapper<I, E, M>>(
    override val typeName: String,
    override val instantProvider: InstantProvider,
    override val repository: R,
    override val mapper: Mapper
) : CRUDService<I, E, M, M, M, R, Mapper>(typeName, instantProvider, repository, mapper)
