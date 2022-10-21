package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CommentTest {

    private final Comment first = Comment.builder()
            .id(1L)
            .text("first")
            .build();
    private final Comment second = Comment.builder()
            .id(1L)
            .text("second")
            .build();

    private final Comment third = Comment.builder()
            .id(2L)
            .text("third")
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