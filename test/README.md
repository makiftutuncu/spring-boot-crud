# spring-boot-crud-test

This is the test module of spring-boot-crud. It brings some test utilities and base classes for testing your CRUD applications built with spring-boot-crud-api. See [Contents](#contents) section for details of each class it provides.

## Table of Contents

1. [Getting Started](#getting-started)
2. [Contents](#contents)

## Getting Started

To get started, add spring-boot-crud-test as a test dependency to your project.

For Gradle with Kotlin DSL, add following to `build.gradle.kts`:

```kotlin
dependencies {
  testImplementation('dev.akif:spring-boot-crud-test:0.5.0')
}
```

For Gradle, add following to `build.gradle`:

```kotlin
dependencies {
  testImplementation 'dev.akif:spring-boot-crud-test:0.5.0'
}
```

For Maven, add following to your `pom.xml`:

```xml
<dependencies>
  <dependency>
    <groupId>dev.akif</groupId>
    <artifactId>spring-boot-crud-test</artifactId>
    <version>0.5.0</version>
    <scope>test</scope>
  </dependency>
</dependencies>
```

## Contents

There are two main things spring-boot-crud-test provides: a test environment with utilities to be used in tests and base test classes which give you test cases for free, the same way spring-boot-crud-api gives you an API for free.

### 1. Testing Environment

Setting up a proper testing environment can be hard. In tests, especially in unit tests, you'll need mock/dummy/custom implementations of certain components. spring-boot-crud-test tries to make it easier for you to write tests for your applications by providing a few of those.

For the remainder of this document, we'll follow the example from [spring-boot-crud-api](../api/README.md) documentation so we'll add tests to our cats API.

#### 1.1. [AdjustableInstantProvider](src/main/kotlin/dev/akif/crud/AdjustableInstantProvider.kt)

There is an [InstantProvider](../api/src/main/kotlin/dev/akif/crud/common/InstantProvider.kt) component used for getting the current `java.time.Instant`. `AdjustableInstantProvider` is a custom implementation of this that can be used in tests. It provides:

* A constant time so every time `now` is called, the same `Instant` is returned
* An `adjust` method to modify the time so the passage of time can be simulated in tests
* A `reset` method to reset the modifications made to the time

#### 1.2. [CRUDTestData](src/main/kotlin/dev/akif/crud/CRUDTestData.kt)

This lets you organize your test data and gives you some test utilities. Things to highlight are:

* Instances of `InMemoryCRUDRepository` and `AdjustableInstantProvider` are created for you.
* You're required to have 3 different instances of your entity as test data (namely `testEntity1`, `testEntity2` and `testEntity3`). This is to have a minimal setup allowing pagination. You can always define more by setting the `moreTestEntities` array.
* Since there are entities and entities are mutable by nature, there is a `copy` method so a copy of an entity can be created. This is useful in creating expected cases in assertions.
* There is a `randomId` method to generate a random id of your entity's id type.
* There is an `areDuplicates` method for defining the uniqueness condition of your entities.
* There are `xToY` methods for mapping between different versions of an entity (with or without some modifications). This is useful when testing multiple layers.

A test data class for cats could be implemented as:

```kotlin
import dev.akif.crud.CRUDTestData
import java.util.UUID

class CatTestData : CRUDTestData<UUID, CatEntity, Cat, CreateCat, UpdateCat, CatTestData>(typeName = "Cat") {
    private val catId1 = UUID.randomUUID()
    private val catId2 = UUID.randomUUID()
    private val catId3 = UUID.randomUUID()

    override val testEntity1: CatEntity =
        CatEntity(
            id = catId1,
            name = "Cookie",
            breed = "Tabby",
            age = 4,
            version = 0,
            createdAt = now,
            updatedAt = now,
            deletedAt = null
        )

    override val testEntity2: CatEntity =
        CatEntity(
            id = catId2,
            name = "Kitty",
            breed = "Persian",
            age = 3,
            version = 0,
            createdAt = now.plusSeconds(1),
            updatedAt = now.plusSeconds(1),
            deletedAt = null
        )

    override val testEntity3: CatEntity =
        CatEntity(
            id = catId3,
            name = "Meowth",
            breed = "Scottish Fold",
            age = 2,
            version = 0,
            createdAt = now.plusSeconds(2),
            updatedAt = now.plusSeconds(2),
            deletedAt = null
        )

    override val moreTestEntities: Array<CatEntity> =
        emptyArray()

    override fun areDuplicates(e1: CatEntity, e2: CatEntity): Boolean =
        e1.name == e2.name
                && e1.breed == e2.breed
                && e1.age == e2.age

    override fun copy(entity: CatEntity): CatEntity =
        CatEntity(
            id = entity.id,
            name = entity.name,
            breed = entity.breed,
            age = entity.age,
            version = entity.version,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            deletedAt = entity.deletedAt
        )

    override fun randomId(): UUID =
        UUID.randomUUID()

    override fun entityToCreateModel(entity: CatEntity): CreateCat =
        CreateCat(
            name = entity.name!!,
            breed = entity.breed!!,
            age = entity.age!!
        )

    override fun entityToUpdateModelWithModifications(entity: CatEntity): UpdateCat =
        UpdateCat(
            name = "${entity.name}-updated",
            age = entity.age?.plus(1) ?: 1
        )

    override fun entityToUpdateModelWithNoModifications(entity: CatEntity): UpdateCat =
        UpdateCat(
            name = entity.name!!,
            age = entity.age!!
        )
}
```

#### 1.3. [InMemoryCRUDRepository](src/main/kotlin/dev/akif/crud/InMemoryCRUDRepository.kt)

This is a map-based in-memory implementation of [CRUDRepository](../api/src/main/kotlin/dev/akif/crud/CRUDRepository.kt) to use in tests. It implements the repository methods the same way a real database. It supports having initial entities and `clean`/`reset` for specific test cases.

### 2. Unit Tests

Unit tests are fundamental in software testing. While writing unit tests, we focus on a single unit. In a CRUD application, this would entail different components at different layers. spring-boot-crud-test provides base classes for such tests.

#### 2.1. [CRUDServiceTest](src/main/kotlin/dev/akif/crud/CRUDServiceTest.kt)

This is a base class for the unit test of a service class. It provides the required set-up and following test cases for free:

* creating new entities
  * should fail with already exists error when trying to create an entity that already exists
  * should create a new entity with the same data of a deleted entity and return it
  * should create a new entity and return it
* getting all entities
  * should return at least 3 entities with default pagination
  * should not return deleted entities
  * should return correct entities with pagination
  * should return empty page when no entities exist
* getting an entity
  * should return null when trying to get a deleted entity
  * should return correct entity
  * should return null when trying to get an entity that doesn't exist
* updating an entity
  * should fail with already exists error when trying to update an entity as duplicate of another entity
  * should fail with not found error when trying to update an entity that doesn't exist
  * should update return updated entity
  * should update with the same data of a deleted entity return updated entity
* deleting an entity
  * should fail with not found error when trying to delete an entity that doesn't exist
  * should delete

A unit test for `CatService` could be implemented as:

```kotlin
import dev.akif.crud.CRUDServiceTest
import org.junit.jupiter.api.DisplayName
import java.util.UUID

@DisplayName("CatService")
class CatServiceTest : CRUDServiceTest<UUID, CatEntity, Cat, CreateCat, UpdateCat, CatMapper, CatRepository, CatService, CatTestData>(
    typeName = "Cat",
    mapper = CatMapper(),
    testData = CatTestData()
) {
    override fun buildService(mapper: CatMapper, testData: CatTestData): CatService =
        CatService(testData.instantProvider, testData.repository, mapper)
}
```

Please note that you can use given dependencies `mapper` and `testData` to build `CatService`. These are the dependencies `CRUDServiceTest` builds for you internally, which are reset for every test case.
