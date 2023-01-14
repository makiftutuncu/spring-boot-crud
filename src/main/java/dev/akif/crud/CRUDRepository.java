package dev.akif.crud;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.Optional;

/**
 * Base interface of a CRUD repository for data layer
 * <p>
 * This is meant to be implemented from a @{@link org.springframework.stereotype.Repository} interface,
 * ideally with {@link org.springframework.data.repository.Repository}
 *
 * @param <I> Id type of the data
 * @param <M> Model type of the data
 * @param <E> Entity type of the data
 */
public interface CRUDRepository<
        I extends Serializable,
        M extends BaseModel<I>,
        E extends BaseEntity<I, M>> {
    /**
     * Finds all entities for given pagination and deletion flag
     *
     * @param pageable {@link Pageable} for the pagination
     * @param deleted  Whether to find deleted entities
     *
     * @return {@link Page} of found entities
     */
    Page<E> findAllByDeleted(final Pageable pageable, final boolean deleted);

    /**
     * Finds an entity with given id and deletion flag
     *
     * @param id      Id of the entity to find
     * @param deleted Whether to find deleted entities
     *
     * @return Found entity in an {@link Optional}
     */
    Optional<E> findByIdAndDeleted(final I id, final boolean deleted);

    /**
     * Saves (creates or updates) given entity
     *
     * @param entity Entity to save
     *
     * @return Saved entity
     */
    E save(final E entity);

    /** Flushes changes made to the entities, meaning any change will be persisted. */
    void flush();
}
