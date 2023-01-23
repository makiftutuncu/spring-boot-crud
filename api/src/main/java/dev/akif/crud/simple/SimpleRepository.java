package dev.akif.crud.simple;

import dev.akif.crud.CRUDRepository;

import java.io.Serializable;

/**
 * Simple variant of {@link CRUDRepository}
 * <p>
 * This is meant to be implemented from a @{@link org.springframework.stereotype.Repository} interface.
 *
 * @param <I> Id type of the data
 * @param <E> Entity type of the data which is a {@link SimpleEntity}
 */
public interface SimpleRepository<I extends Serializable, E extends SimpleEntity<I, E>> extends CRUDRepository<I, E> {
}
