package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ItemRequestTest {
    private final ItemRequest first = ItemRequest.builder()
            .id(1L)
            .description("first")
            .build();
    private final ItemRequest second = ItemRequest.builder()
            .id(1L)
            .description("second")
            .build();

    private final ItemRequest third = ItemRequest.builder()
            .id(2L)
            .description("third")
            .build();

    @Test
    void testEquals() {
        assertEquals(first, second);
        assertNotEquals(first, third);
        assertNotEquals(first, null);
        assertNotEquals(first, new Object());
    }

    @Test
    void testHashCode() {
        assertEquals(first.hashCode(), second.hashCode());
        assertNotEquals(first.hashCode(), third.hashCode());
    }


}