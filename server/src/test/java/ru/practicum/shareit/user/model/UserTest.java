package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class UserTest {
    private final User first = User.builder()
            .id(1L)
            .name("First")
            .build();
    private final User second = User.builder()
            .id(1L)
            .name("Second")
            .build();

    private final User third = User.builder()
            .id(2L)
            .name("Third")
            .build();

    @Test
    void equalsTest() {
        assertEquals(first, second);
        assertNotEquals(first, third);
        assertNotEquals(first, null);
        assertNotEquals(first, new Object());
    }

    @Test
    void hashCodeTest() {
        assertEquals(first.hashCode(), second.hashCode());
        assertNotEquals(first.hashCode(), third.hashCode());
    }
}