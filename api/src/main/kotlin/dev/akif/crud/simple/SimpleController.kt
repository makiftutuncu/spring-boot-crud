package dev.akif.crud.simple

import dev.akif.crud.CRUDController
import java.io.Serializable

/**
 * Simple variant of [CRUDController] where create/update models are just models
 * and create/update DTOs are just DTOs
 *
 * This is meant to be extended from a **@RestController** class,
 * ideally also with a **@RequestMapping** with some path prefix for the endpoints.
 *
 * @param I      Id type of the data
 * @param E      Entity type of the data which is a [SimpleEntity]
 * @param M      Model type of the data which is a [SimpleModel]
 * @param D      DTO type of the data which is a [SimpleDTO]
 * @param Mapper Mapper type of the data which is a [SimpleMapper]
 * @param S      Service type of the data which is a [SimpleService]
 */
abstract class SimpleController<
        I : Serializable,
        E : SimpleEntity<I, E>,
        M : SimpleModel<I>,
        D : SimpleDTO<I>,
        out Mapper : SimpleMapper<I, E, M, D>,
        out S : SimpleService<I, E, M, D, Mapper>>(
    override val typeName: String,
    override val service: S,
    override val mapper: Mapper
) : CRUDController<I, E, M, D, M, M, D, D, Mapper, Mapper, S>(typeName, service, mapper)
