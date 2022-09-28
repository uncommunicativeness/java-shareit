package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.validators.DateConstraint;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@DateConstraint
public class BookingInDto {
    Long itemId;

    @NotNull
    LocalDateTime start;

    @NotNull
    LocalDateTime end;
}
