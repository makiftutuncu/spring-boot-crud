package dev.akif.crud

import dev.akif.crud.common.Paged
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Paged")
class PagedTest {
    @DisplayName("should convert each item in the page to build a new Paged")
    @Test
    fun testMap() {
        val paged = Paged(listOf("a", "ab", "abc"), 0, 3, 1)
        val (data, page, perPage, totalPages) = paged.map { it.length }
        Assertions.assertEquals(listOf(1, 2, 3), data)
        Assertions.assertEquals(0, page)
        Assertions.assertEquals(3, perPage)
        Assertions.assertEquals(1, totalPages)
    }
}
