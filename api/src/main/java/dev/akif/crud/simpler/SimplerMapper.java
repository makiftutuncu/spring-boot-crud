package dev.akif.crud.simpler;

import dev.akif.crud.CRUDDTOMapper;
import dev.akif.crud.CRUDMapper;

import java.io.Serializable;

/**
 * Simpler variant of {@link CRUDMapper} where create/update models and DTOs are just models,
 * which is also a {@link CRUDDTOMapper}
 *
 * @param <I> ID type of the entity
 * @param <E> Entity type
 * @param <M> Model type
 */
public interface SimplerMapper<
        I extends Serializable,
        E extends SimplerEntity<I, E>,
        M extends SimplerModel<I, E, M>> extends CRUDMapper<I, E, M, M, M>, CRUDDTOMapper<I, E, M, M, M, M, M, M> {
    @Override
    default M modelToDTO(final M model) {
        return model;
    }
}
