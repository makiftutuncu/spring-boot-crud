package dev.akif.crud.simpler;

import dev.akif.crud.CRUDEntity;

import java.io.Serializable;

/**
 * Simpler variant of {@link CRUDEntity}
 *
 * @param <I> Id type of the data
 * @param <E> Entity type, meant to be the exact type extending this class
 */
public abstract class SimplerEntity<I extends Serializable, E extends CRUDEntity<I, E>> extends CRUDEntity<I, E> {
    /**
     * Default empty constructor
     */
    public SimplerEntity() {}
}
