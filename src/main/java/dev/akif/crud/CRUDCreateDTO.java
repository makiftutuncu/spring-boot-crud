package dev.akif.crud;

import java.io.Serializable;

public interface CRUDCreateDTO<
        I extends Serializable,
        M extends CRUDModel<I>,
        E extends CRUDEntity<I, M>,
        CM extends CRUDCreateModel<I, M, E>> {
    CM toCreateModel();
}
