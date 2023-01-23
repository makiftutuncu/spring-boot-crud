package dev.akif.crud.simplest;

import dev.akif.crud.CRUDDTOMapper;
import dev.akif.crud.CRUDMapper;

import java.io.Serializable;

/**
 * The simplest variant of {@link CRUDMapper} where the only data model is the entity,
 * which is also a {@link CRUDDTOMapper}
 *
 * @param <I> ID type of the entity
 * @param <E> Entity type
 */
public interface SimplestMapper<
        I extends Serializable,
        E extends SimplestEntity<I, E>> extends CRUDMapper<I, E, E, E, E>, CRUDDTOMapper<I, E, E, E, E, E, E, E> {
    @Override
    default E modelToDTO(final E model) {
        return model;
    }

    @Override
    default E entityToModel(final E entity) {
        return entity;
    }
}
