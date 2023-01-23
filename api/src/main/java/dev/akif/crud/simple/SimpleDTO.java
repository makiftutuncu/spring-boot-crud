package dev.akif.crud.simple;

import dev.akif.crud.*;

import java.io.Serializable;

/**
 * Simple version of {@link CRUDDTO} where create/update DTOs are just DTOs directly
 *
 * @param <I> Id type of the data
 * @param <M> Model type of the data which is also a {@link CRUDCreateModel} and a {@link CRUDUpdateModel}
 * @param <E> Entity type of the data
 */
public interface SimpleDTO<
        I extends Serializable,
        M extends CRUDModel<I> & CRUDCreateModel<I, E> & CRUDUpdateModel<I, E>,
        E extends CRUDEntity<I, E>>
        extends
        CRUDDTO<I>,
        CRUDCreateDTO<I, E, M>,
        CRUDUpdateDTO<I, E, M> {
}
