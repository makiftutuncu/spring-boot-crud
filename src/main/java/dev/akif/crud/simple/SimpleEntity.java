package dev.akif.crud.simple;

import dev.akif.crud.CRUDEntity;

import java.io.Serializable;

/**
 * Simple variant of {@link CRUDEntity}
 *
 * @param <I> Id type of the data
 * @param <E> Entity type, meant to be the exact type extending this class
 */
public abstract class SimpleEntity<I extends Serializable, E extends CRUDEntity<I, E>> extends CRUDEntity<I, E> {
    /**
     * Default empty constructor
     */
    public SimpleEntity() {}
}
