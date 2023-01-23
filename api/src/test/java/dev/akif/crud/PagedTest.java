package dev.akif.crud;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Paged")
class PagedTest {
    @DisplayName("should convert each item in the page to build a new Paged")
    @Test
    void testMap() {
        Paged<String> paged = new Paged<>(List.of("a", "ab", "abc"), 0, 3, 1);
        Paged<Integer> mapped = paged.map(String::length);

        assertEquals(List.of(1, 2, 3), mapped.data());
        assertEquals(0, mapped.page());
        assertEquals(3, mapped.perPage());
        assertEquals(1, mapped.totalPages());
    }
}
