package ru.practicum.shareit.item.dto.item;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ItemOutDto {
    Long id;
    String name;
    String description;
    Boolean available;
    Set<Comment> comments;
    Long requestId;

    @Data
    @Builder
    public static class Comment {
        Long id;
        String text;
        String authorName;
        LocalDateTime created;
    }
}
