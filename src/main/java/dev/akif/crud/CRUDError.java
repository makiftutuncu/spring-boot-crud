package dev.akif.crud;

import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public record CRUDError(
        @NotNull int code,
        @NotNull String message,
        @NotNull String type
) {
    public CRUDError(final HttpStatus status, final String message) {
        this(status.value(), message, status.getReasonPhrase());
    }

    public ResponseEntity<CRUDError> toResponseEntity() {
        return ResponseEntity.status(code).contentType(MediaType.APPLICATION_JSON).body(this);
    }

    public static <D> CRUDErrorException alreadyExists(final String what, final D data) {
        return new CRUDErrorException(
                new CRUDError(HttpStatus.CONFLICT, "%s with %s already exists.".formatted(what, data))
        );
    }

    public static <I> CRUDErrorException notFound(final String what, final I id) {
        return new CRUDErrorException(
                new CRUDError(HttpStatus.NOT_FOUND, "%s with id %s is not found.".formatted(what, id))
        );
    }
}
