package dev.akif.crud.error;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.http.HttpStatus;

/**
 * Wrapper for a {@link CRUDError} as a {@link RuntimeException} so it can be thrown
 * <p>
 * The stack trace and suppression is disabled by default.
 */
@EqualsAndHashCode(callSuper = true)
@Value
public class CRUDErrorException extends RuntimeException {
    /**
     * {@link CRUDError} that occurred
     */
    CRUDError error;

    /**
     * Constructor to create a {@link CRUDErrorException} from given error
     *
     * @param error {@link CRUDError}
     */
    public CRUDErrorException(final CRUDError error) {
        super(error.toString(), null, false, false);
        this.error = error;
    }

    /**
     * Builds an "already exists" error as a {@link CRUDErrorException} with "409 Conflict" HTTP status for given data
     *
     * @param what Type of what already exists
     * @param data Data describing what already exists
     * @param <D>  Type of the data describing what already exists
     * @return {@link CRUDErrorException} containing {@link CRUDError} built from given data
     */
    public static <D> CRUDErrorException alreadyExists(final String what, final D data) {
        return new CRUDErrorException(
                new CRUDError(HttpStatus.CONFLICT, "%s with %s already exists.".formatted(what, data))
        );
    }

    /**
     * Builds a "not found" error as a {@link CRUDErrorException} with "404 Not Found" HTTP status for given id
     *
     * @param what Type of what is not found
     * @param id   Id of what is not found
     * @param <I>  Type of the id of what is not found
     * @return {@link CRUDErrorException} containing {@link CRUDError} built from given data
     */
    public static <I> CRUDErrorException notFound(final String what, final I id) {
        return new CRUDErrorException(
                new CRUDError(HttpStatus.NOT_FOUND, "%s with id %s is not found.".formatted(what, id))
        );
    }
}
