package dev.akif.crud.simplest;

import dev.akif.crud.CRUDRepository;

import java.io.Serializable;

/**
 * The simplest variant of {@link CRUDRepository}
 * <p>
 * This is meant to be implemented from a @{@link org.springframework.stereotype.Repository} interface.
 *
 * @param <I> Id type of the data
 * @param <E> Entity type of the data which is a {@link SimplestEntity}
 */
public interface SimplestRepository<I extends Serializable, E extends SimplestEntity<I, E>>
        extends CRUDRepository<I, E> {
}
