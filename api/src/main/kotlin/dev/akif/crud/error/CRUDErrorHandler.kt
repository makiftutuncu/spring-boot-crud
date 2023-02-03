package dev.akif.crud.error

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler

/**
 * Default ExceptionHandler implementations for [CRUDErrorException] and unexpected exceptions
 *
 * This is meant to be implemented from a **@RestControllerAdvice** class.
 */
interface CRUDErrorHandler {
    /**
     * Converts [CRUDError] in this exception into a ResponseEntity with correct status code, media type and body
     *
     * @param cee [CRUDErrorException]
     * @return ResponseEntity built from [CRUDError] in this exception
     */
    @ExceptionHandler(CRUDErrorException::class)
    fun handleCRUDError(cee: CRUDErrorException): ResponseEntity<CRUDError> {
        val e = cee.error
        return ResponseEntity.status(e.code).contentType(MediaType.APPLICATION_JSON).body(e)
    }

    /**
     * Converts any exception into a ResponseEntity with 500 Internal Server Error code and a generic message
     *
     * @param ex [Exception]
     * @return ResponseEntity built from given exception
     */
    @ExceptionHandler(Exception::class)
    fun handleUnexpectedError(ex: Exception): ResponseEntity<CRUDError> {
        val message = "An unexpected error occurred!"
        log.error(message, ex)
        val e = CRUDError(HttpStatus.INTERNAL_SERVER_ERROR, message)
        return ResponseEntity.status(e.code).contentType(MediaType.APPLICATION_JSON).body(e)
    }

    /** @suppress */
    companion object {
        @JvmStatic
        val log: Logger = LoggerFactory.getLogger(CRUDErrorHandler::class.java)
    }
}
