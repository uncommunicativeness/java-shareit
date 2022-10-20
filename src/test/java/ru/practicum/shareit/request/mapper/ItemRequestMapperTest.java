package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ItemRequestMapperTest {
    private final ItemRequestInDto itemRequestInDto = ItemRequestInDto.builder()
            .description("Description")
            .build();
    private final ItemRequest itemRequest = ItemRequest.builder()
            .id(1L)
            .description("Description")
            .items(Set.of(Item.builder().id(1L).build()))
            .build();
    private final ItemRequestOutDto itemRequestOutDto = ItemRequestOutDto.builder()
            .id(1L)
            .description("Description")
            .items(Set.of(ItemRequestOutDto.Item.builder().id(1L).requestId(1L).build()))
            .build();
    @InjectMocks
    private ItemRequestMapper mapper;

    @Test
    void toItemRequest() {
        assertEquals(itemRequest.getDescription(), mapper.toItemRequest(itemRequestInDto).getDescription());
    }

    @Test
    void toDto() {
        assertEquals(itemRequestOutDto, mapper.toDto(itemRequest));
    }
}