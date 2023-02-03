package dev.akif.crud.error

import org.springframework.http.HttpStatus

/**
 * Wrapper for a [CRUDError] as a [RuntimeException] so it can be thrown
 *
 * The stack trace and suppression is disabled by default.
 *
 * @property error [CRUDError] to wrap
 */
data class CRUDErrorException(val error: CRUDError) : RuntimeException(error.toString(), null, false, false) {
    /** @suppress */
    companion object {
        /**
         * Builds an "already exists" error as a [CRUDErrorException] with "409 Conflict" HTTP status for given data
         *
         * @param what Type of what already exists
         * @param data Data describing what already exists
         * @return [CRUDErrorException] containing [CRUDError] built from given data
         */
        @JvmStatic
        fun alreadyExists(what: String, data: Any): CRUDErrorException {
            return CRUDErrorException(
                CRUDError(HttpStatus.CONFLICT, "$what with $data already exists.")
            )
        }

        /**
         * Builds a "not found" error as a [CRUDErrorException] with "404 Not Found" HTTP status for given id
         *
         * @param what Type of what is not found
         * @param id   Id of what is not found
         * @return [CRUDErrorException] containing [CRUDError] built from given data
         */
        @JvmStatic
        fun notFound(what: String, id: Any): CRUDErrorException {
            return CRUDErrorException(
                CRUDError(HttpStatus.NOT_FOUND, "$what with id $id is not found.")
            )
        }
    }
}
