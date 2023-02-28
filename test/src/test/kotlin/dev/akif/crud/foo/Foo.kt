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
import java.util.UUID

class FooTestData : CRUDTestData<UUID, FooEntity, Foo, CreateFoo, UpdateFoo, FooTestData>(typeName = "Foo") {
    private val fooId1 = UUID.randomUUID()
    private val fooId2 = UUID.randomUUID()
    private val fooId3 = UUID.randomUUID()

    override val testEntity1: FooEntity =
        FooEntity(
            id = fooId1,
            foo = "Foo 1",
            bar = 1,
            version = 0,
            createdAt = now,
            updatedAt = now,
            deletedAt = null
        )

    override val testEntity2: FooEntity =
        FooEntity(
            id = fooId2,
            foo = "Foo 2",
            bar = 2,
            version = 0,
            createdAt = now.plusSeconds(1),
            updatedAt = now.plusSeconds(1),
            deletedAt = null
        )

    override val testEntity3: FooEntity =
        FooEntity(
            id = fooId3,
            foo = "Foo 3",
            bar = 3,
            version = 0,
            createdAt = now.plusSeconds(2),
            updatedAt = now.plusSeconds(2),
            deletedAt = null
        )

    override val moreTestEntities: Array<FooEntity> =
        emptyArray()

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

    override fun randomId(): UUID =
        UUID.randomUUID()

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
    repository: CRUDRepository<UUID, FooEntity>,
    mapper: FooMapper
): CRUDService<UUID, FooEntity, Foo, CreateFoo, UpdateFoo, FooRepository, FooMapper>(
    typeName = "Foo",
    instantProvider = instantProvider,
    crudRepository = repository,
    mapper = mapper
)

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
            id = requireNotNull(entity.id) { "id was null." },
            foo = requireNotNull(entity.foo) { "foo was null." },
            bar = requireNotNull(entity.bar) { "bar was null." },
            version = requireNotNull(entity.version) { "version was null." },
            createdAt = requireNotNull(entity.createdAt) { "createdAt was null." },
            updatedAt = requireNotNull(entity.updatedAt) { "updatedAt was null." },
            deletedAt = entity.deletedAt
        )

    override fun updateEntityWith(entity: FooEntity, updateModel: UpdateFoo) {
        entity.apply {
            foo = updateModel.foo
            bar = updateModel.bar
        }
    }

    override fun createDTOToCreateModel(createDTO: CreateFooDTO): CreateFoo =
        CreateFoo(
            foo = createDTO.foo,
            bar = createDTO.bar
        )

    override fun modelToDTO(model: Foo): FooDTO =
        FooDTO(
            id = model.id,
            foo = model.foo,
            bar = model.bar,
            createdAt = model.createdAt,
            updatedAt = model.updatedAt
        )

    override fun updateDTOToUpdateModel(updateDTO: UpdateFooDTO): UpdateFoo =
        UpdateFoo(
            foo = updateDTO.foo,
            bar = updateDTO.bar
        )
}

@Repository
interface FooRepository: CRUDRepository<UUID, FooEntity>

@JakartaEntity
class FooEntity(
    @Id override var id: UUID? = null,
    var foo: String? = null,
    var bar: Int? = null,
    override var version: Int? = null,
    override var createdAt: Instant? = null,
    override var updatedAt: Instant? = null,
    override var deletedAt: Instant? = null
): CRUDEntity<UUID>(id, version, createdAt, updatedAt, deletedAt) {
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
