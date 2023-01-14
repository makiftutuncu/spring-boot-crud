package dev.akif.crud;

import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;

/**
 * Error model of the API operations
 *
 * @param code    Numeric code matching the HTTP status code of the response this error should produce
 * @param message Error message describing what went wrong
 * @param type    Error type matching the HTTP status of the response this error should produce
 */
public record CRUDError(
        @NotNull int code,
        @NotNull String message,
        @NotNull String type
) {
    /**
     * Constructor to create a {@link CRUDError} with given data
     *
     * @param status  {@link HttpStatus} to use for the response this error should produce
     * @param message Error message describing what went wrong
     */
    public CRUDError(final HttpStatus status, final String message) {
        this(status.value(), message, status.getReasonPhrase());
    }
}
