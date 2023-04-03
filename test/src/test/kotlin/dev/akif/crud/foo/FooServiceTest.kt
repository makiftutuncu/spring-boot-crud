package dev.akif.crud.foo

import dev.akif.crud.CRUDServiceTest
import org.junit.jupiter.api.DisplayName
import java.util.UUID

@DisplayName("FooService")
class FooServiceTest: CRUDServiceTest<UUID, FooEntity, Foo, CreateFoo, UpdateFoo, FooMapper, FooRepository, FooService, FooTestData>(
    mapper = FooMapper(),
    testData = FooTestData
) {
    override fun buildService(mapper: FooMapper, testData: FooTestData): FooService =
        FooService(testData.instantProvider, InMemoryFooRepository, mapper)
}
