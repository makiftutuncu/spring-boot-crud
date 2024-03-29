package dev.akif.crud.common

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

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
     * Constructor to build a [Paged] from a list of data, applying given pagination
     *
     * @param data     Data to page
     * @param pageable Requested pagination info
     */
    constructor(data: List<A>, pageable: Pageable) : this(PageImpl<A>(data, pageable, data.size.toLong()))

    /**
     * Builds a new [Paged] by converting every item in this by given mapping function
     *
     * @param B      Type to which mapping function converts
     * @param mapper Conversion function
     * @return New [Paged] containing converted items
     */
    fun <B> map(mapper: (A) -> B): Paged<B> =
        Paged(
            data.map(mapper),
            page,
            perPage,
            totalPages
        )

    /** @suppress */
    companion object {
        /**
         * Builds an empty [Paged]
         *
         * @param page       Requested page number
         * @param perPage    Requested number of items in a page
         * @param totalPages Requested number of total pages available
         * @return Empty [Paged] with given pagination info
         */
        @JvmStatic
        fun <A> empty(page: Int, perPage: Int, totalPages: Int): Paged<A> =
            Paged(emptyList(), page, perPage, totalPages)

        /**
         * Builds an empty [Paged]
         *
         * @param pageable Requested pagination info
         * @return Empty [Paged] with given pagination info
         */
        @JvmStatic
        fun <A> empty(pageable: Pageable): Paged<A> =
            Paged(emptyList(), pageable.pageNumber, pageable.pageSize, 0)
    }
}
