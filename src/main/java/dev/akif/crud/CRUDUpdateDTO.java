package dev.akif.crud;

import java.io.Serializable;

public interface CRUDUpdateDTO<
        I extends Serializable,
        M extends CRUDModel<I>,
        E extends CRUDEntity<I, M>,
        UM extends CRUDUpdateModel<I, M, E>> {
    UM toUpdateModel();
}
