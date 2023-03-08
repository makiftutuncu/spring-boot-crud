package dev.akif.crud.common

import jakarta.servlet.http.HttpServletRequest

/**
 * Container of request parameters containing path variables and query parameters
 *
 * @property path  Path variables
 * @property query Query parameters
 */
data class Parameters(val path: Map<String, String>, val query: Map<String, List<String>>) {
    /**
     * Constructor to build a [Parameters] from a [HttpServletRequest]
     *
     * @param pathVariables Path variables
     * @param request HTTP request
     */
    constructor(pathVariables: Map<String, String>, request: HttpServletRequest) : this(
        pathVariables, request.parameterMap.mapValues { it.value.toList() }.toMap()
    )

    /**
     * Gets a path variable by given name
     *
     * @param name Name of the path variable
     * @return Value of the path variable or null if it doesn't exist
     */
    fun pathVariable(name: String): String? =
        path[name]

    /**
     * Gets a path variable by given name converted to given type
     *
     * @param name Name of the path variable
     * @param converter Conversion function
     * @return Value of the path variable as given type or null if it doesn't exist
     * @throws IllegalArgumentException If conversion fails
     */
    inline fun <reified A> pathVariable(name: String, converter: (String) -> A?): A? =
        path[name]?.let {
            runCatching { converter(it) }.getOrElse {
                throw IllegalArgumentException("Cannot get '$name' path variable, conversion failed", it)
            }
        }

    /**
     * Gets values of a query parameter by given name
     *
     * @param name Name of the query parameter
     * @return Values of the query parameter or empty list if it doesn't exist
     */
    fun queryParameters(name: String): List<String> =
        query[name] ?: emptyList()

    /**
     * Gets the first value of a query parameter by given name
     *
     * @param name Name of the query parameter
     * @return First value of the query parameter or null if it doesn't exist
     */
    fun queryParameter(name: String): String? =
        query[name]?.firstOrNull()

    /**
     * Gets the first value of a query parameter by given name converted to given type
     *
     * @param name Name of the query parameter
     * @param converter Conversion function
     * @return First value of the query parameter as given type or null if it doesn't exist
     * @throws IllegalArgumentException If conversion fails
     */
    inline fun <reified A> queryParameter(name: String, converter: (String) -> A?): A? =
        query[name]?.firstOrNull()?.let {
            runCatching { converter(it) }.getOrElse {
                throw IllegalArgumentException("Cannot get '$name' query parameter, conversion failed", it)
            }
        }
}
