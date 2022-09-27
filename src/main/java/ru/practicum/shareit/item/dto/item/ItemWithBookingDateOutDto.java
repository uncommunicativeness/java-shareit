package ru.practicum.shareit.item.dto.item;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class ItemWithBookingDateOutDto {
    Long id;
    String name;
    String description;
    Boolean available;
    Booking lastBooking;
    Booking nextBooking;
    Set<Comment> comments;

    @Data
    @Builder
    public static class Booking {
        Long id;
        Long bookerId;
    }

    @Data
    @Builder
    public static class Comment {
        Long id;
        String text;
        String authorName;
        LocalDateTime created;
    }
}
