package dev.akif.crud

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.Repository
import java.io.Serializable
import java.util.*

/**
 * Base interface of a CRUD repository for data layer
 *
 * This is meant to be implemented from a **@Repository** interface.
 *
 * @param I Id type of the data
 * @param E Entity type of the data which is a [CRUDEntity]
 */
@NoRepositoryBean
interface CRUDRepository<I : Serializable, E : CRUDEntity<I, E>> : Repository<E, I> {
    /**
     * Finds all entities for given pagination and deletion flag
     *
     * @param pageable Pageable for the pagination
     * @return Page of found entities
     */
    fun findAllByDeletedAtIsNull(pageable: Pageable): Page<E>

    /**
     * Finds an entity with given id and deletion flag
     *
     * @param id Id of the entity to find
     * @return Found entity in an [Optional]
     */
    fun findByIdAndDeletedAtIsNull(id: I): E?

    /**
     * Saves given entity, used when creating a new entity
     *
     * @param entity Entity to save
     * @return Saved entity
     */
    @Modifying
    fun save(entity: E): E

    /**
     * Updates given entity if it is at given version
     *
     * @param entity  Entity to save
     * @param version Expected version of the entity before the update
     * @return Number of affected rows
     */
    @Modifying
    fun updateByVersion(entity: E, version: Int): Int

    /**
     * Flushes changes made to the entities, meaning any change will be persisted.
     */
    fun flush()
}
