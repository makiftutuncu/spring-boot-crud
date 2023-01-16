package dev.akif.crud.simpler;

import dev.akif.crud.*;

import java.io.Serializable;

/**
 * Simpler variant of {@link CRUDModel} which is also a {@link CRUDCreateModel},
 * a {@link CRUDUpdateModel}, a {@link CRUDDTO}, a {@link CRUDCreateDTO} and a {@link CRUDUpdateDTO}
 *
 * @param <I> Id type of the data
 * @param <E> Entity type of the data
 * @param <M> Model type which is also a {@link CRUDCreateModel}, a {@link CRUDUpdateModel},
 *            meant to be the exact type implementing this interface
 */
public interface SimplerModel<
        I extends Serializable,
        E extends CRUDEntity<I, E>,
        M extends CRUDModel<I> & CRUDCreateModel<I, E> & CRUDUpdateModel<I, E>>
        extends
        CRUDModel<I>,
        CRUDCreateModel<I, E>,
        CRUDUpdateModel<I, E>,
        CRUDDTO<I>,
        CRUDCreateDTO<I, E, M>,
        CRUDUpdateDTO<I, E, M> {
}
