package ru.practicum.shareit.request.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ItemRequestMapper {
    public ItemRequest toItemRequest(ItemRequestInDto dto) {
        return ItemRequest.builder()
                .description(dto.getDescription())
                .build();
    }

    public ItemRequestOutDto toDto(ItemRequest itemRequest) {
        ItemRequestOutDto dto = ItemRequestOutDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();

        if (itemRequest.getItems() != null) {
            dto.setItems(itemRequest.getItems()
                    .stream()
                    .filter(Objects::nonNull)
                    .map(item -> ItemRequestOutDto.Item.builder()
                            .id(item.getId())
                            .name(item.getName())
                            .description(item.getDescription())
                            .available(item.getAvailable())
                            .requestId(dto.getId())
                            .build()).collect(Collectors.toSet()));
        }

        return dto;
    }
}
