package dev.akif.crud;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public record Paged<A>(
        @NotNull List<A> data,
        @NotNull int page,
        @NotNull int perPage,
        @NotNull int totalPages
) {
    public Paged(final Page<A> page) {
        this(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages()
        );
    }

    public <B> Paged<B> map(final Function<A, B> mapper) {
        return new Paged<>(
                data.stream().map(mapper).toList(),
                page,
                perPage,
                totalPages
        );
    }
}
