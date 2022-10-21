package ru.practicum.shareit.item.dto.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ItemWithBookingDateOutDto {
    Long id;
    String name;
    String description;
    Boolean available;
    Booking lastBooking;
    Booking nextBooking;
    Set<Comment> comments;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Booking {
        Long id;
        Long bookerId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Comment {
        Long id;
        String text;
        String authorName;
        LocalDateTime created;
    }
}
