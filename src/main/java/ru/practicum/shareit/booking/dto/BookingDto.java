package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.validators.DateConstraint;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@DateConstraint
public class BookingDto {
    Long id;

    @NotNull
    LocalDateTime start;

    @NotNull
    LocalDateTime end;

    Booking.Status status;

    Long itemId;

    Long bookerId;

    ItemDto item;

    UserDto booker;
}
