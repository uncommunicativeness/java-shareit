package ru.practicum.shareit.item.dto.item;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class ItemInDto {
    @NotBlank
    String name;

    @NotBlank
    String description;

    @NotNull
    Boolean available;

    Long requestId;
}
