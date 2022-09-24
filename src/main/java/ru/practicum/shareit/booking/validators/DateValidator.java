package ru.practicum.shareit.booking.validators;

import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class DateValidator implements ConstraintValidator<DateConstraint, BookingDto> {
    @Override
    public boolean isValid(BookingDto bookingDto, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime currentDate = LocalDateTime.now();

        return (bookingDto.getStart().isAfter(currentDate) || bookingDto.getStart().isEqual(currentDate))
                && (bookingDto.getEnd().isAfter(currentDate) || bookingDto.getEnd().isEqual(currentDate))
                && (bookingDto.getStart().isBefore(bookingDto.getEnd()) || bookingDto.getEnd().isEqual(currentDate));
    }
}