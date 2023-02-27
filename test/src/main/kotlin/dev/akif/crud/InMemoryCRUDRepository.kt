package dev.akif.crud

import dev.akif.crud.error.CRUDErrorException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.io.Serializable

/**
 * In-memory implementation of [CRUDRepository] for testing
 *
 * @param I        Id type of the data
 * @param E        Entity type of the data which is a [CRUDEntity]
 * @param CM       Create model type of the data which is a [CRUDCreateModel]
 * @param TestData Test data type of the data which is a [CRUDTestData]
 *
 * @property typeName Type name of the data
 * @property testData Mapper dependency of this test which is a [CRUDTestData]
 */
class InMemoryCRUDRepository<
        I : Serializable,
        E : CRUDEntity<I>,
        CM : CRUDCreateModel,
        out TestData : CRUDTestData<I, E, *, CM, *>>(
    private val typeName: String,
    private val testData: TestData
) : CRUDRepository<I, E> {
    /**
     * Map of entities by their ids, accessible for tests
     */
    val entities: MutableMap<I, E> = mutableMapOf()

    init {
        testData.testEntity1.apply { entities[id!!] = this }
        testData.testEntity2.apply { entities[id!!] = this }
        testData.testEntity3.apply { entities[id!!] = this }
        testData.moreTestEntities.forEach { it.apply { entities[id!!] = this } }
    }

    override fun findAllByDeletedAtIsNull(pageable: Pageable): Page<E> =
        PageImpl(
            entities
                .values
                .stream()
                .filter { it.deletedAt == null }
                .skip(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .map(testData::copy)
                .toList(),
            pageable,
            entities.count { (_, e) -> e.deletedAt == null }.toLong()
        )

    override fun findByIdAndDeletedAtIsNull(id: I): E? =
        entities[id]?.takeIf { it.deletedAt == null }?.let(testData::copy)

    override fun save(entity: E): E {
        handleDuplicates(entity, testData.entityToCreateModel(entity))
        val copied = testData.copy(entity)
        entity.id?.also { entities[it] = copied }
        return copied
    }

    override fun update(entity: E): Int {
        handleDuplicates(entity, testData.entityToUpdateModelWithNoModifications(entity))
        val existing = entity.id?.let { entities[it] }?.takeIf { it.version == entity.version }
        val copied = testData.copy(entity).apply { version = version?.plus(1) }
        return existing
            ?.let { entity.id?.let { id -> entities.replace(id, copied) } }
            ?.let { 1 }
            ?: 0
    }

    /**
     * Clears the repository for testing
     */
    fun clear() {
        entities.clear()
    }

    private fun handleDuplicates(entity: E, data: Any) {
        val found = entities.values.any { e ->
            entity.deletedAt == null
                && e.deletedAt == null
                && testData.areDuplicates(entity, e)
        }
        if (found) {
            throw CRUDErrorException.alreadyExists(typeName, data)
        }
    }
}
