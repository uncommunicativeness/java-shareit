package ru.practicum.shareit.request.dto;

import lombok.*;


@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemRequestInDto {
    String description;
}
