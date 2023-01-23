package dev.akif.crud;

import java.io.Serializable;

/**
 * Base model that contains required data to create a new entity
 *
 * @param <I> Id type of the data
 * @param <E> Entity type of the data
 */
public interface CRUDCreateModel<I extends Serializable, E extends CRUDEntity<I, E>> {
}
