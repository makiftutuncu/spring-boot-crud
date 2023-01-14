package dev.akif.crud;

import java.io.Serializable;

public interface CRUDUpdateModel<
        I extends Serializable,
        M extends CRUDModel<I>,
        E extends CRUDEntity<I, M>> {
    void applyUpdatesTo(final E entity);
}
