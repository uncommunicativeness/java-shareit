package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingOutDto {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    Status status;
    Booker booker;
    Item item;

    public enum Status {
        WAITING,
        APPROVED,
        REJECTED,
        CANCELED
    }

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
