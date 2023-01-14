package dev.akif.crud;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Default {@link ExceptionHandler} implementation for {@link CRUDErrorException}
 * <p>
 * This is meant to be implemented from a {@link org.springframework.web.bind.annotation.RestControllerAdvice} class.
 */
public interface CRUDErrorHandler {
    /**
     * Converts {@link CRUDError} in this exception into a {@link ResponseEntity} with correct status code, media type and body
     *
     * @param cee {@link CRUDErrorException}
     *
     * @return {@link ResponseEntity} built from {@link CRUDError} in this exception
     */
    @ExceptionHandler(CRUDErrorException.class)
    default ResponseEntity<CRUDError> handleCRUDError(final CRUDErrorException cee) {
        final var e = cee.getError();
        return ResponseEntity.status(e.code()).contentType(MediaType.APPLICATION_JSON).body(e);
    }
}
