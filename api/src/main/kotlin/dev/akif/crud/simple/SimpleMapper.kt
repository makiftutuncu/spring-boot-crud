package dev.akif.crud.simple

import dev.akif.crud.CRUDDTOMapper
import dev.akif.crud.CRUDMapper
import java.io.Serializable

/**
 * Simple variant of [CRUDMapper] where create/update models are just models,
 * which is also a [CRUDDTOMapper]
 *
 * @param I Id type of the entity
 * @param E Entity type of the data which is a [SimpleEntity]
 * @param M Model type of the data which is a [SimpleModel]
 * @param D DTO type of the data which is a [SimpleDTO]
 */
interface SimpleMapper<
        I : Serializable,
        E : SimpleEntity<I, E>,
        M : SimpleModel<I>,
        D : SimpleDTO<I>> : CRUDMapper<I, E, M, M, M>, CRUDDTOMapper<I, M, D, M, M, D, D>
