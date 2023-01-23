package dev.akif.crud.error;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("CRUDErrorException")
public class CRUDErrorExceptionTest {
    @DisplayName("should create an already exists error with 409 Conflict status and correct message")
    @Test
    void testAlreadyExists() {
        final var e = CRUDErrorException.alreadyExists("Test", "some test data");

        assertEquals(HttpStatus.CONFLICT.value(), e.getError().code());
        assertEquals(HttpStatus.CONFLICT.getReasonPhrase(), e.getError().type());
        assertEquals("Test with some test data already exists.", e.getError().message());
    }

    @DisplayName("should create a not found error with 404 Not Found status and correct message")
    @Test
    void testNotFound() {
        final var e = CRUDErrorException.notFound("Test", "foo");

        assertEquals(HttpStatus.NOT_FOUND.value(), e.getError().code());
        assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), e.getError().type());
        assertEquals("Test with id foo is not found.", e.getError().message());
    }
}
