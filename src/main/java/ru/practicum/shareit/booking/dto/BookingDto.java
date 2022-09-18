package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDate;

@Data
public class BookingDto {
    Long id;
    LocalDate start;
    LocalDate end;
    Booking.Status status;
    ItemDto item;
    UserDto user;
}
