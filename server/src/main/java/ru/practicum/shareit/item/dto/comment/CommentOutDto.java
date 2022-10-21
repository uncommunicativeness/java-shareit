package ru.practicum.shareit.item.dto.comment;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentOutDto {
    Long id;
    String text;
    String authorName;
    LocalDateTime created;
}
