package ru.practicum.shareit.item.dto.item;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ItemInDto {
    @NotBlank
    String name;

    @NotBlank
    String description;

    @NotNull
    Boolean available;
}
