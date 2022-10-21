package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
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

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Booker {
        Long id;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Item {
        Long id;
        String name;
    }
}
