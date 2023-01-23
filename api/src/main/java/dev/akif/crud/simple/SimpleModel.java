package dev.akif.crud.simple;

import dev.akif.crud.CRUDCreateModel;
import dev.akif.crud.CRUDEntity;
import dev.akif.crud.CRUDModel;
import dev.akif.crud.CRUDUpdateModel;

import java.io.Serializable;

/**
 * Simple variant of {@link CRUDModel} which is also a {@link CRUDCreateModel} and a {@link CRUDUpdateModel}
 *
 * @param <I> Id type of the data
 * @param <E> Entity type of the data
 */
public interface SimpleModel<I extends Serializable, E extends CRUDEntity<I, E>>
        extends CRUDModel<I>, CRUDCreateModel<I, E>, CRUDUpdateModel<I, E> {
}
