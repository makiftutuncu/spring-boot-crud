package dev.akif.crud.error;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("CRUDError")
class CRUDErrorTest {
    @DisplayName("should assign correct code and type when constructed from an HttpStatus")
    @Test
    void testConstruction() {
        final var e = new CRUDError(HttpStatus.BAD_REQUEST, "Test Message");

        assertEquals(HttpStatus.BAD_REQUEST.value(), e.code());
        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), e.type());
        assertEquals("Test Message", e.message());
    }
}
