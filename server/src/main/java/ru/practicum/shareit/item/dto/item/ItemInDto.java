package ru.practicum.shareit.item.dto.item;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ItemInDto {
    String name;

    String description;

    Boolean available;

    Long requestId;
}
