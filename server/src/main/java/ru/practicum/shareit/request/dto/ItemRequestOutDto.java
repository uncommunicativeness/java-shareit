package ru.practicum.shareit.request.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "id")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
