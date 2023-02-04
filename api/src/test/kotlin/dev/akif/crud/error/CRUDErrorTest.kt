package dev.akif.crud.error

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

@DisplayName("CRUDError")
class CRUDErrorTest {
    @DisplayName("should assign correct code and type when constructed from an HttpStatus")
    @Test
    fun testConstruction() {
        val (code, message, type) = CRUDError(HttpStatus.BAD_REQUEST, "Test Message")
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), code)
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.reasonPhrase, type)
        Assertions.assertEquals("Test Message", message)
    }
}
