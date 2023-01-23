package dev.akif.crud.simpler;

import dev.akif.crud.CRUDRepository;

import java.io.Serializable;

/**
 * Simpler variant of {@link CRUDRepository}
 * <p>
 * This is meant to be implemented from a @{@link org.springframework.stereotype.Repository} interface.
 *
 * @param <I> Id type of the data
 * @param <E> Entity type of the data which is a {@link SimplerEntity}
 */
public interface SimplerRepository<I extends Serializable, E extends SimplerEntity<I, E>> extends CRUDRepository<I, E> {
}
