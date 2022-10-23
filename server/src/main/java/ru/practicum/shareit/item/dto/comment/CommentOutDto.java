package ru.practicum.shareit.item.dto.comment;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "id")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentOutDto {
    Long id;
    String text;
    String authorName;
    LocalDateTime created;
}
