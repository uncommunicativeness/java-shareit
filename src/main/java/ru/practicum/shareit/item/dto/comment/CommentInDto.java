package ru.practicum.shareit.item.dto.comment;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentInDto {
    @NotBlank
    String text;
}
