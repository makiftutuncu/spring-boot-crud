package dev.akif.crud;

import java.io.Serializable;

/**
 * Base DTO that contains required data to update an existing entity
 *
 * @param <I>  Id type of the data
 * @param <M>  Model type of the data
 * @param <E>  Entity type of the data
 * @param <UM> Update model type of the data
 */
public interface BaseUpdateDTO<
        I extends Serializable,
        M extends BaseModel<I>,
        E extends BaseEntity<I, M>,
        UM extends BaseUpdateModel<I, M, E>> {
    /**
     * Mapper to convert this update DTO into a update model
     *
     * @return Update model built from this update DTO
     */
    UM toUpdateModel();
}
