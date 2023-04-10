package dev.akif.crud.foo

import dev.akif.crud.*
import dev.akif.crud.common.InstantProvider
import jakarta.persistence.Id
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import jakarta.persistence.Entity as JakartaEntity
import dev.akif.crud.CRUDTestData
import dev.akif.crud.common.Paged
import dev.akif.crud.common.Parameters
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.util.UUID

object InMemoryFooRepository: InMemoryCRUDRepository<UUID, FooEntity, CreateFoo, FooTestData>(FooTestData), FooRepository

object FooTestData : CRUDTestData<UUID, FooEntity, Foo, CreateFoo, UpdateFoo, FooTestData>(typeName = "Foo") {
    private val fooId1 = UUID.randomUUID()
    private val fooId2 = UUID.randomUUID()
    private val fooId3 = UUID.randomUUID()

    override val repository: InMemoryCRUDRepository<UUID, FooEntity, CreateFoo, FooTestData>
        get() = InMemoryFooRepository

    override val idGenerator: IdGenerator<UUID> =
        IdGenerator.uuid

    override val testEntity1: FooEntity =
        FooEntity(
            id = fooId1,
            foo = "Foo 1",
            bar = 1,
            version = 0,
            createdAt = now(),
            updatedAt = now(),
            deletedAt = null
        )

    override val testEntity2: FooEntity =
        FooEntity(
            id = fooId2,
            foo = "Foo 2",
            bar = 2,
            version = 0,
            createdAt = now().plusSeconds(1),
            updatedAt = now().plusSeconds(1),
            deletedAt = null
        )

    override val testEntity3: FooEntity =
        FooEntity(
            id = fooId3,
            foo = "Foo 3",
            bar = 3,
            version = 0,
            createdAt = now().plusSeconds(2),
            updatedAt = now().plusSeconds(2),
            deletedAt = null
        )

    override val moreTestEntities: Array<FooEntity> =
        emptyArray()

    override val defaultFirstPageEntities: List<FooEntity> =
        listOf(
            testEntity1,
            testEntity2,
            testEntity3
        )

    override val paginationTestCases: List<Pair<PageRequest, Paged<FooEntity>>> =
        listOf(
            PageRequest.of(0, 2) to Paged(
                data = listOf(testEntity1, testEntity2),
                page = 0,
                perPage = 2,
                totalPages = 2
            ),
            PageRequest.of(1, 2) to Paged(
                data = listOf(testEntity3),
                page = 1,
                perPage = 2,
                totalPages = 2
            ),
            PageRequest.of(2, 2) to Paged.empty(page = 2, perPage = 2, totalPages = 2)
        )

    override val testParameters: Parameters =
        Parameters.empty

    override fun areDuplicates(e1: FooEntity, e2: FooEntity): Boolean =
        e1.foo == e2.foo && e1.bar == e2.bar

    override fun copy(entity: FooEntity): FooEntity =
        FooEntity(
            id = entity.id,
            foo = entity.foo,
            bar = entity.bar,
            version = entity.version,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            deletedAt = entity.deletedAt
        )

    override fun entityToCreateModel(entity: FooEntity): CreateFoo =
        CreateFoo(
            foo = entity.foo!!,
            bar = entity.bar!!
        )

    override fun entityToUpdateModelWithModifications(entity: FooEntity): UpdateFoo =
        UpdateFoo(
            foo = "${entity.foo}-updated",
            bar = entity.bar?.plus(1) ?: 1
        )

    override fun entityToUpdateModelWithNoModifications(entity: FooEntity): UpdateFoo =
        UpdateFoo(
            foo = entity.foo!!,
            bar = entity.bar!!
        )
}

@RestController
@RequestMapping("/foos")
class FooController(service: FooService, mapper: FooMapper): CRUDController<UUID, FooEntity, Foo, FooDTO, CreateFoo, UpdateFoo, CreateFooDTO, UpdateFooDTO, FooMapper, FooMapper, FooRepository, FooService>(
    typeName = "Foo",
    service = service,
    mapper = mapper
)

@Service
class FooService(
    instantProvider: InstantProvider,
    repository: FooRepository,
    mapper: FooMapper
): CRUDService<UUID, FooEntity, Foo, CreateFoo, UpdateFoo, FooRepository, FooMapper>(
    typeName = "Foo",
    instantProvider = instantProvider,
    repository = repository,
    mapper = mapper
) {
    override fun createUsingRepository(entity: FooEntity, parameters: Parameters): FooEntity =
        repository.save(entity)

    override fun listUsingRepository(pageable: Pageable, parameters: Parameters): Page<FooEntity> =
        repository.findAllByDeletedAtIsNull(pageable)

    override fun getUsingRepository(id: UUID, parameters: Parameters): FooEntity? =
        repository.findByIdAndDeletedAtIsNull(id)

    override fun updateUsingRepository(entity: FooEntity, parameters: Parameters): Int =
        repository.update(entity)
}

@Component
class FooMapper: CRUDMapper<UUID, FooEntity, Foo, CreateFoo, UpdateFoo>, CRUDDTOMapper<UUID, Foo, FooDTO, CreateFoo, UpdateFoo, CreateFooDTO, UpdateFooDTO> {
    override fun entityToBeCreatedFrom(createModel: CreateFoo, now: Instant): FooEntity =
        FooEntity(
            id = UUID.randomUUID(),
            foo = createModel.foo,
            bar = createModel.bar,
            version = 0,
            createdAt = now,
            updatedAt = now,
            deletedAt = null
        )

    override fun entityToModel(entity: FooEntity): Foo =
        Foo(
            id = requireNotNull(entity.id) { "id is required." },
            foo = requireNotNull(entity.foo) { "foo is required." },
            bar = requireNotNull(entity.bar) { "bar is required." },
            version = requireNotNull(entity.version) { "version is required." },
            createdAt = requireNotNull(entity.createdAt) { "createdAt is required." },
            updatedAt = requireNotNull(entity.updatedAt) { "updatedAt is required." },
            deletedAt = entity.deletedAt
        )

    override fun updateEntityWith(entity: FooEntity, updateModel: UpdateFoo) {
        entity.apply {
            foo = updateModel.foo
            bar = updateModel.bar
        }
    }

    override fun createDTOToCreateModel(createDTO: CreateFooDTO, parameters: Parameters): CreateFoo =
        CreateFoo(
            foo = createDTO.foo,
            bar = createDTO.bar
        )

    override fun modelToDTO(model: Foo, parameters: Parameters): FooDTO =
        FooDTO(
            id = model.id,
            foo = model.foo,
            bar = model.bar,
            createdAt = model.createdAt,
            updatedAt = model.updatedAt
        )

    override fun updateDTOToUpdateModel(updateDTO: UpdateFooDTO, parameters: Parameters): UpdateFoo =
        UpdateFoo(
            foo = updateDTO.foo,
            bar = updateDTO.bar
        )
}

@Repository
interface FooRepository: CRUDRepository<UUID, FooEntity>

@JakartaEntity
class FooEntity(
    @Id override var id: UUID?,
    var foo: String?,
    var bar: Int?,
    override var version: Int?,
    override var createdAt: Instant?,
    override var updatedAt: Instant?,
    override var deletedAt: Instant?
): CRUDEntity<UUID>() {
    override fun toString(): String =
        "FooEntity(id=$id, foo=$foo, bar=$bar, version=$version, createdAt=$createdAt, updatedAt=$updatedAt, deletedAt=$deletedAt)"
}

data class CreateFoo(val foo: String, val bar: Int): CRUDCreateModel

data class UpdateFoo(val foo: String, val bar: Int): CRUDUpdateModel

data class Foo(
    val id: UUID,
    val foo: String,
    val bar: Int,
    val version: Int,
    val createdAt: Instant,
    val updatedAt: Instant,
    val deletedAt: Instant?
): CRUDModel<UUID> {
    override fun id(): UUID = id
    override fun version(): Int = version
    override fun createdAt(): Instant = createdAt
    override fun updatedAt(): Instant = updatedAt
    override fun deletedAt(): Instant? = deletedAt
}

data class CreateFooDTO(val foo: String, val bar: Int): CRUDCreateDTO

data class UpdateFooDTO(val foo: String, val bar: Int): CRUDUpdateDTO

data class FooDTO(
    val id: UUID,
    val foo: String,
    val bar: Int,
    val createdAt: Instant,
    val updatedAt: Instant
): CRUDDTO<UUID> {
    override fun id(): UUID = id
    override fun createdAt(): Instant = createdAt
    override fun updatedAt(): Instant = updatedAt
}
