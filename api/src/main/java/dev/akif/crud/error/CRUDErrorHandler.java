package dev.akif.crud.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Default {@link ExceptionHandler} implementations for {@link CRUDErrorException} and unexpected exceptions
 * <p>
 * This is meant to be implemented from a {@link org.springframework.web.bind.annotation.RestControllerAdvice} class.
 */
public interface CRUDErrorHandler {
    /**
     * Logger for this class
     */
    Logger log = LoggerFactory.getLogger(CRUDErrorHandler.class);

    /**
     * Converts {@link CRUDError} in this exception into a {@link ResponseEntity} with correct status code, media type and body
     *
     * @param cee {@link CRUDErrorException}
     * @return {@link ResponseEntity} built from {@link CRUDError} in this exception
     */
    @ExceptionHandler(CRUDErrorException.class)
    default ResponseEntity<CRUDError> handleCRUDError(final CRUDErrorException cee) {
        final var e = cee.getError();
        return ResponseEntity.status(e.code()).contentType(MediaType.APPLICATION_JSON).body(e);
    }

    /**
     * Converts any exception into a {@link ResponseEntity} with 500 Internal Server Error code and a generic message
     *
     * @param ex {@link Exception}
     * @return {@link ResponseEntity} built from given exception
     */
    @ExceptionHandler(Exception.class)
    default ResponseEntity<CRUDError> handleUnexpectedError(final Exception ex) {
        final var message = "An unexpected error occurred!";
        log.error(message, ex);
        final var e = new CRUDError(HttpStatus.INTERNAL_SERVER_ERROR, message);
        return ResponseEntity.status(e.code()).contentType(MediaType.APPLICATION_JSON).body(e);
    }
}
