package ru.practicum.shareit.booking.validators;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingInDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DateValidatorTest {

    private final DateValidator dateValidator = new DateValidator();
    private final BookingInDto bookingInDtoSucces = BookingInDto.builder()
            .start(LocalDateTime.now().plusHours(1))
            .end(LocalDateTime.now().plusDays(1))
            .build();

    private final BookingInDto bookingInDtoFail = BookingInDto.builder()
            .start(LocalDateTime.now().plusDays(1))
            .end(LocalDateTime.now().plusHours(1))
            .build();

    @Test
    void isValidTest() {
        assertTrue(dateValidator.isValid(bookingInDtoSucces, null));
        assertFalse(dateValidator.isValid(bookingInDtoFail, null));
    }
}