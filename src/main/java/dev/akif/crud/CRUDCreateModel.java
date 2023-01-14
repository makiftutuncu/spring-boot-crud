package dev.akif.crud;

import java.io.Serializable;

public interface CRUDCreateModel<
        I extends Serializable,
        M extends CRUDModel<I>,
        E extends CRUDEntity<I, M>> {
    E toEntity();
}
