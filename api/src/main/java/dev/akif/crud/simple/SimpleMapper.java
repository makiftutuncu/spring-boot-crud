package dev.akif.crud.simple;

import dev.akif.crud.CRUDDTOMapper;
import dev.akif.crud.CRUDMapper;

import java.io.Serializable;

/**
 * Simple variant of {@link CRUDMapper} where create/update models are just models,
 * which is also a {@link CRUDDTOMapper}
 *
 * @param <I> ID type of the entity
 * @param <E> Entity type
 * @param <M> Model type
 * @param <D> DTO type
 */
public interface SimpleMapper<
        I extends Serializable,
        E extends SimpleEntity<I, E>,
        M extends SimpleModel<I, E>,
        D extends SimpleDTO<I, M, E>> extends CRUDMapper<I, E, M, M, M>, CRUDDTOMapper<I, E, M, D, M, M, D, D> {
}
