package ru.practicum.shareit.request.dto;

import lombok.*;


@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemRequestInDto {
    String description;
}
