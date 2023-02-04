package dev.akif.crud.error

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

@DisplayName("CRUDErrorHandler")
class CRUDErrorHandlerTest {
    private object TestErrorHandler : CRUDErrorHandler

    @DisplayName("should handle a CRUDError to produce ResponseEntity with correct status code and body")
    @Test
    fun testHandleCRUDError() {
        val e = CRUDError(HttpStatus.BAD_REQUEST.value(), "Test Error", "Test Type")
        val response = TestErrorHandler.handleCRUDError(CRUDErrorException(e))
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        Assertions.assertEquals(e, response.body)
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.headers.contentType)
    }

    @DisplayName("should handle an unexpected Exception to produce ResponseEntity with 500 Internal Server Error code and a generic message")
    @Test
    fun testUnexpectedException() {
        val response = TestErrorHandler.handleUnexpectedError(Exception("test"))
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
        Assertions.assertEquals(
            CRUDError(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred!"),
            response.body
        )
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.headers.contentType)
    }
}
