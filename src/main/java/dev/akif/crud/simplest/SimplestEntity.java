package dev.akif.crud.simplest;

import dev.akif.crud.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * Simpler variant of {@link CRUDEntity} which is also a {@link CRUDModel}, a {@link CRUDCreateModel},
 * a {@link CRUDUpdateModel}, a {@link CRUDDTO}, a {@link CRUDCreateDTO} and a {@link CRUDUpdateDTO}
 *
 * @param <I> Id type of the data
 * @param <E> Entity type which is also a {@link CRUDCreateModel}, a {@link CRUDUpdateModel},
 *            a {@link CRUDCreateDTO} and a {@link CRUDUpdateDTO},
 *            meant to be the exact type extending this class
 */
public abstract class SimplestEntity<
        I extends Serializable,
        E extends CRUDEntity<I, E> & CRUDCreateModel<I, E> & CRUDUpdateModel<I, E> & CRUDCreateDTO<I, E, E> & CRUDUpdateDTO<I, E, E>>
        extends CRUDEntity<I, E>
        implements
        CRUDModel<I>,
        CRUDCreateModel<I, E>,
        CRUDUpdateModel<I, E>,
        CRUDDTO<I>,
        CRUDCreateDTO<I, E, E>,
        CRUDUpdateDTO<I, E, E> {
    /**
     * Default empty constructor
     */
    public SimplestEntity() {}

    @Override
    public I id() {
        return getId();
    }

    @Override
    public int version() {
        return getVersion();
    }

    @Override
    public Instant createdAt() {
        return getCreatedAt();
    }

    @Override
    public Instant updatedAt() {
        return getUpdatedAt();
    }

    @Override
    public Instant deletedAt() {
        return getDeletedAt();
    }
}
