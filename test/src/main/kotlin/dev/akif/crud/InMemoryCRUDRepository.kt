package dev.akif.crud

import dev.akif.crud.error.CRUDErrorException.Companion.alreadyExists
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.io.Serializable
import java.util.function.BiPredicate

/**
 * In-memory implementation of [CRUDRepository] for testing
 *
 * @param I      Id type of the data
 * @param E      Entity type of the data which is a [CRUDEntity]
 * @param CM     Create model type of the data which is a [CRUDCreateModel]
 * @param Mapper Mapper type of the data which is a [CRUDTestMapper]
 *
 * @property typeName         Type name of the data
 * @property mapper           Mapper dependency of this test which is a [CRUDTestMapper]
 * @property duplicateCheck   [BiPredicate] to check if two entities are duplicates
 * @property initialEntities  Initial test entities in the repository
 */
class InMemoryCRUDRepository<
        I : Serializable,
        E : CRUDEntity<I, E>,
        CM : CRUDCreateModel,
        out Mapper : CRUDTestMapper<I, E, *, CM, *>>(
    private val typeName: String,
    private val mapper: Mapper,
    private val duplicateCheck: BiPredicate<E, E>,
    private vararg val initialEntities: E
) : CRUDRepository<I, E> {
    private val entities: MutableMap<I, E> = mutableMapOf()

    init {
        initialEntities.forEach {
            it.id?.also { id ->
                entities[id] = it
            }
        }
    }

    override fun findAllByDeletedAtIsNull(pageable: Pageable): Page<E> =
        PageImpl(
            entities
                .values
                .stream()
                .filter { it.deletedAt == null }
                .skip(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .toList(),
            pageable,
            entities.size.toLong()
        )

    override fun findByIdAndDeletedAtIsNull(id: I): E? =
        entities[id]?.takeIf { it.deletedAt == null }

    override fun save(entity: E): E {
        handleDuplicates(entity)
        entity.id?.also { entities[it] = entity }
        return entity
    }

    override fun update(entity: E): Int {
        handleDuplicates(entity)
        return entity.id
            ?.let { entities[it] }
            ?.takeIf { it.version == entity.version }
            ?.let { entity.id?.let { id -> entities.replace(id, entity) } }
            ?.let { 1 }
            ?: 0
    }

    /**
     * Clears the repository for testing
     */
    fun clear() {
        entities.clear()
    }

    private fun handleDuplicates(entity: E) {
        if (entities.values.any { e -> duplicateCheck.test(entity, e) }) {
            throw alreadyExists(typeName, mapper.entityToCreateModel(entity))
        }
    }
}
