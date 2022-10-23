package ru.practicum.shareit.item.dto.item;

import lombok.*;


@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemInDto {
    String name;

    String description;

    Boolean available;

    Long requestId;
}
