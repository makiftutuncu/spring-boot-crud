package dev.akif.crud.error

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.http.HttpStatus

/**
 * Error model of the API operations
 *
 * @property code    Numeric code matching the HTTP status code of the response this error should produce
 * @property message Error message describing what went wrong
 * @property type    Error type matching the HTTP status of the response this error should produce
 */
@JvmRecord
data class CRUDError(
    @JsonIgnore @JvmField val code: Int,
    @JvmField val message: String,
    @JvmField val type: String
) {
    /**
     * Constructor to create a [CRUDError] with given data
     *
     * @param status  [HttpStatus] to use for the response this error should produce
     * @param message Error message describing what went wrong
     */
    constructor(status: HttpStatus, message: String) : this(status.value(), message, status.reasonPhrase)
}
