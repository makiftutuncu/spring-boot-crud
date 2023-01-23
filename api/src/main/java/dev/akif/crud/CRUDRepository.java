package dev.akif.crud;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.util.Optional;

/**
 * Base interface of a CRUD repository for data layer
 * <p>
 * This is meant to be implemented from a @{@link org.springframework.stereotype.Repository} interface.
 *
 * @param <I> Id type of the data
 * @param <E> Entity type of the data
 */
@NoRepositoryBean
public interface CRUDRepository<I extends Serializable, E extends CRUDEntity<I, E>> extends Repository<E, I> {
    /**
     * Finds all entities for given pagination and deletion flag
     *
     * @param pageable {@link Pageable} for the pagination
     * @return {@link Page} of found entities
     */
    Page<E> findAllByDeletedAtIsNull(final Pageable pageable);

    /**
     * Finds an entity with given id and deletion flag
     *
     * @param id Id of the entity to find
     * @return Found entity in an {@link Optional}
     */
    Optional<E> findByIdAndDeletedAtIsNull(final I id);

    /**
     * Saves given entity, used when creating a new entity
     *
     * @param entity Entity to save
     * @return Saved entity
     */
    @Modifying
    E save(final E entity);

    /**
     * Updates given entity if it is at given version
     *
     * @param entity  Entity to save
     * @param version Expected version of the entity before the update
     * @return Number of affected rows
     */
    @Modifying
    int updateByVersion(final E entity, final int version);

    /**
     * Flushes changes made to the entities, meaning any change will be persisted.
     */
    void flush();
}
