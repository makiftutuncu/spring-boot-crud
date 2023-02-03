package dev.akif.crud.error

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

@DisplayName("CRUDErrorException")
class CRUDErrorExceptionTest {
    @DisplayName("should create an already exists error with 409 Conflict status and correct message")
    @Test
    fun testAlreadyExists() {
        val e = CRUDErrorException.alreadyExists("Test", "some test data")
        assertEquals(HttpStatus.CONFLICT.value(), e.error.code)
        assertEquals(HttpStatus.CONFLICT.reasonPhrase, e.error.type)
        assertEquals("Test with some test data already exists.", e.error.message)
    }

    @DisplayName("should create a not found error with 404 Not Found status and correct message")
    @Test
    fun testNotFound() {
        val e = CRUDErrorException.notFound("Test", "foo")
        assertEquals(HttpStatus.NOT_FOUND.value(), e.error.code)
        assertEquals(HttpStatus.NOT_FOUND.reasonPhrase, e.error.type)
        assertEquals("Test with id foo is not found.", e.error.message)
    }
}
