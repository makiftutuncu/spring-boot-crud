package dev.akif.crud

import dev.akif.crud.error.CRUDErrorException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
 * @property testData Mapper dependency of this test which is a [CRUDTestData]
 */
open class InMemoryCRUDRepository<
        I : Serializable,
        E : CRUDEntity<I>,
        CM : CRUDCreateModel,
        TestData : CRUDTestData<I, E, *, CM, *, TestData>>(
    protected val testData: TestData
) : CRUDRepository<I, E> {
    private val log: Logger by lazy {
        LoggerFactory.getLogger(javaClass)
    }

    /**
     * Map of entities by their ids, accessible for tests
     */
    val entities: MutableMap<I, E> = mutableMapOf()

    init {
        reset()
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
        if (entity.id == null) {
            entity.id = testData.idGenerator.next()
        }
        val copied = testData.copy(entity)
        entities[entity.id!!] = copied
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

    override fun flush() {
        log.debug("Flushed repository: $entities")
    }

    /**
     * Clears the repository for testing, there will be no entities left after this.
     */
    fun clear() {
        entities.clear()
    }

    /**
     * Reset the repository for testing, there will be the initial entities after this.
     */
    fun reset() {
        entities.clear()
        entities[testData.testEntity1.id!!] = testData.testEntity1
        entities[testData.testEntity2.id!!] = testData.testEntity2
        entities[testData.testEntity3.id!!] = testData.testEntity3
        testData.moreTestEntities.forEach { testEntity ->
            entities[testEntity.id!!] = testEntity
        }
        log.debug("Reset repository: $entities")
    }

    private fun handleDuplicates(entity: E, data: Any) {
        val found = entities.values.any { e ->
            entity.deletedAt == null
                && e.deletedAt == null
                && testData.areDuplicates(entity, e)
        }
        if (found) {
            throw CRUDErrorException.alreadyExists(testData.typeName, data)
        }
    }
}
