package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class ItemRequestOutDto {
    Long id;
    String description;
    LocalDateTime created;
    Set<Item> items;

    @Data
    @Builder
    public static class Item {
        Long id;
        String name;
        String description;
        Boolean available;
        Long requestId;
    }
}
