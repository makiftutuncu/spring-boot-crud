package dev.akif.crud.simpler

import dev.akif.crud.CRUDController
import java.io.Serializable

/**
 * Simpler variant of [CRUDController] where create/update models and DTOs are just models
 *
 * This is meant to be extended from a **@RestController** class,
 * ideally also with a **@RequestMapping** with some path prefix for the endpoints.
 *
 * @param I      Id type of the data
 * @param E      Entity type of the data which is a [SimplerEntity]
 * @param M      Model type of the data which is a [SimplerModel]
 * @param Mapper Mapper type of the data which is a [SimplerMapper]
 * @param R      Repository type of the data which is a [SimplerRepository]
 * @param S      Service type of the data which is a [SimplerService]
 */
abstract class SimplerController<
        I : Serializable,
        E : SimplerEntity<I>,
        M : SimplerModel<I>,
        out Mapper : SimplerMapper<I, E, M>,
        out R : SimplerRepository<I, E>,
        out S : SimplerService<I, E, M, R, Mapper>>(
    override val typeName: String,
    override val service: S,
    override val mapper: Mapper
) : CRUDController<I, E, M, M, M, M, M, M, Mapper, Mapper, R, S>(typeName, service, mapper)
