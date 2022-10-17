package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class BookingTest {
    private final Booking first = Booking.builder()
            .id(1L)
            .build();

    private final Booking second = Booking.builder()
            .id(1L)
            .build();

    private final Booking third = Booking.builder()
            .id(2L)
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