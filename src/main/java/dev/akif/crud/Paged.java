package dev.akif.crud;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

/**
 * Container of paged data with its pagination info
 *
 * @param data       Data in current page
 * @param page       Current page number
 * @param perPage    Number of items in a page
 * @param totalPages Number of total pages available
 *
 * @param <A> Type of the data in this page
 */
public record Paged<A>(
        @NotNull List<A> data,
        @NotNull int page,
        @NotNull int perPage,
        @NotNull int totalPages
) {
    /**
     * Constructor to build a {@link Paged} from a {@link Page}
     *
     * @param page {@link Page}
     */
    public Paged(final Page<A> page) {
        this(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages()
        );
    }

    /**
     * Converts this {@link Paged} into a {@link Paged} containing
     * items converted by given mapping function
     *
     * @param mapper Conversion function
     *
     * @return New {@link Paged} containing converted items
     *
     * @param <B> Type to which mapping function converts
     */
    public <B> Paged<B> map(final Function<A, B> mapper) {
        return new Paged<>(
                data.stream().map(mapper).toList(),
                page,
                perPage,
                totalPages
        );
    }
}
