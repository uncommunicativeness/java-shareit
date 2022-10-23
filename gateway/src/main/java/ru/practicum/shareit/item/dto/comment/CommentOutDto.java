package ru.practicum.shareit.item.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentOutDto {
    Long id;
    String text;
    String authorName;
    LocalDateTime created;
}
