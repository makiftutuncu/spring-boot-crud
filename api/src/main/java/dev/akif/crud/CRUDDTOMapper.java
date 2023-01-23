package dev.akif.crud;

import java.io.Serializable;

/**
 * Mapper to convert between DTOs, models and entities
 *
 * @param <I>  Id type of the data
 * @param <E>  Entity type of the data
 * @param <M>  Model type of the data
 * @param <D>  DTO type of the data
 * @param <CM> Create model type of the data
 * @param <UM> Update model type of the data
 * @param <CD> Create DTO type of the data
 * @param <UD> Update DTO type of the data
 */
public interface CRUDDTOMapper<
        I extends Serializable,
        E extends CRUDEntity<I, E>,
        M extends CRUDModel<I>,
        D extends CRUDDTO<I>,
        CM extends CRUDCreateModel<I, E>,
        UM extends CRUDUpdateModel<I, E>,
        CD extends CRUDCreateDTO<I, E, CM>,
        UD extends CRUDUpdateDTO<I, E, UM>> {
    /**
     * Mapper to convert from model to DTO
     *
     * @param model Model to convert
     * @return DTO built from given model
     */
    D modelToDTO(final M model);

    /**
     * Mapper to convert given create DTO into a create model
     *
     * @param createDTO Create DTO to convert
     * @return Create model built from this create DTO
     */
    CM createDTOToCreateModel(final CD createDTO);

    /**
     * Mapper to convert given update DTO into a update model
     *
     * @param updateDTO Update DTO to convert
     * @return Create model built from this update DTO
     */
    UM updateDTOToUpdateModel(final UD updateDTO);
}
