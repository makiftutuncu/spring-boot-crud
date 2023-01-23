package dev.akif.crud.error;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("CRUDErrorHandler")
class CRUDErrorHandlerTest {
    private enum TestErrorHandler implements CRUDErrorHandler {
        instance;
    }

    @DisplayName("should handle a CRUDError to produce ResponseEntity with correct status code and body")
    @Test
    void testHandleCRUDError() {
        final var e = new CRUDError(HttpStatus.BAD_REQUEST.value(), "Test Error", "Test Type");
        final var response = TestErrorHandler.instance.handleCRUDError(new CRUDErrorException(e));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(e, response.getBody());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    }

    @DisplayName("should handle an unexpected Exception to produce ResponseEntity with 500 Internal Server Error code and a generic message")
    @Test
    void testUnexpectedException() {
        final var response = TestErrorHandler.instance.handleUnexpectedError(new Exception("test"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(new CRUDError(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred!"), response.getBody());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    }
}
