package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingOutDto {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    Booking.Status status;
    Booker booker;
    Item item;

    @Data
    @Builder
    public static class Booker {
        Long id;
    }

    @Data
    @Builder
    public static class Item {
        Long id;
        String name;
    }
}
