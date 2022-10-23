package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ItemRequestOutDto {
    Long id;
    String description;
    LocalDateTime created;
    Set<Item> items;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Item {
        Long id;
        String name;
        String description;
        Boolean available;
        Long requestId;
    }
}
