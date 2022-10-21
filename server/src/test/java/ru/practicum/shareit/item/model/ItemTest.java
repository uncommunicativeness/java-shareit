package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ItemTest {
    private final Item first = Item.builder()
            .id(1L)
            .name("first")
            .build();
    private final Item second = Item.builder()
            .id(1L)
            .name("second")
            .build();

    private final Item third = Item.builder()
            .id(2L)
            .name("third")
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