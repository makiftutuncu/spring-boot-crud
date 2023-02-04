package dev.akif.crud

import org.springframework.data.domain.Page

/**
 * Container of paged data with its pagination info
 *
 * @param A Type of the data in this page
 *
 * @property data       Data in current page
 * @property page       Current page number
 * @property perPage    Number of items in a page
 * @property totalPages Number of total pages available
 */
@JvmRecord
data class Paged<out A>(
    @JvmField val data: List<A>,
    @JvmField val page: Int,
    @JvmField val perPage: Int,
    @JvmField val totalPages: Int
) {
    /**
     * Constructor to build a [Paged] from a Spring Page
     *
     * @param page Page
     */
    constructor(page: Page<A>) : this(
        page.content,
        page.number,
        page.size,
        page.totalPages
    )

    /**
     * Builds a new [Paged] by converting every item in this by given mapping function
     *
     * @param B      Type to which mapping function converts
     * @param mapper Conversion function
     * @return New [Paged] containing converted items
     */
    fun <B> map(mapper: (A) -> B): Paged<B> {
        return Paged(
            data.map(mapper),
            page,
            perPage,
            totalPages
        )
    }
}
