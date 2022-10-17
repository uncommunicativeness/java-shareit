package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.exception.exceptions.BadRequestException;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ItemRequestServiceTest {
    private final LocalDateTime created = LocalDateTime.now();
    private final ItemRequest itemRequest = ItemRequest.builder()
            .id(1L)
            .description("Description")
            .created(created)
            .requestor(User.builder().id(1L).build())
            .items(Set.of(Item.builder().id(1L).build()))
            .build();

    private final ItemRequestInDto itemRequestInDto = ItemRequestInDto.builder()
            .description("Description")
            .build();

    private final ItemRequestOutDto itemRequestOutDto = ItemRequestOutDto.builder()
            .id(1L)
            .description("Description")
            .created(created)
            .items(Set.of(ItemRequestOutDto.Item.builder().id(1L).build()))
            .build();

    @InjectMocks
    private ItemRequestService itemRequestService;

    @Mock
    private ItemRequestMapper itemRequestMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Test
    void save() {
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(itemRequestRepository.save(itemRequest)).thenReturn(itemRequest);
        when(userRepository.findById(1L)).thenReturn(Optional.of(User.builder().id(1L).build()));
        when(itemRequestMapper.toItemRequest(itemRequestInDto)).thenReturn(itemRequest);
        when(itemRequestMapper.toDto(itemRequest)).thenReturn(itemRequestOutDto);

        ItemRequestOutDto out = itemRequestService.save(1L, itemRequestInDto);
        assertEquals(itemRequestOutDto, out);
    }

    @Test
    void saveWithoutRequestorId() {
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(itemRequestRepository.save(itemRequest)).thenReturn(itemRequest);
        when(userRepository.findById(1L)).thenReturn(Optional.of(User.builder().id(1L).build()));
        when(itemRequestMapper.toItemRequest(itemRequestInDto)).thenReturn(itemRequest);
        when(itemRequestMapper.toDto(itemRequest)).thenReturn(itemRequestOutDto);

        assertThrows(BadRequestException.class, () -> itemRequestService.save(null, itemRequestInDto));
    }

    @Test
    void saveWithoutUser() {
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(itemRequestRepository.save(itemRequest)).thenReturn(itemRequest);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(itemRequestMapper.toItemRequest(itemRequestInDto)).thenReturn(itemRequest);
        when(itemRequestMapper.toDto(itemRequest)).thenReturn(itemRequestOutDto);

        assertThrows(NotFoundException.class, () -> itemRequestService.save(1L, itemRequestInDto));
    }

    @Test
    void findByOwnId() {
        when(itemRequestRepository.findByRequestorId(anyLong(), any())).thenReturn(List.of(itemRequest));
        when(userRepository.findById(1L)).thenReturn(Optional.of(User.builder().build()));
        when(itemRequestMapper.toItemRequest(itemRequestInDto)).thenReturn(itemRequest);
        when(itemRequestMapper.toDto(itemRequest)).thenReturn(itemRequestOutDto);

        List<ItemRequestOutDto> list = itemRequestService.findByOwnId(1L);
        assertEquals(List.of(itemRequestOutDto), list);
    }

    @Test
    void findByOwnIdWithoutRequestorId() {
        when(itemRequestRepository.findByRequestorId(anyLong(), any())).thenReturn(List.of(itemRequest));
        when(userRepository.findById(1L)).thenReturn(Optional.of(User.builder().build()));
        when(itemRequestMapper.toItemRequest(itemRequestInDto)).thenReturn(itemRequest);
        when(itemRequestMapper.toDto(itemRequest)).thenReturn(itemRequestOutDto);

        assertThrows(BadRequestException.class, () -> itemRequestService.findByOwnId(null));
    }

    @Test
    void findByOwnIdWithoutUser() {
        when(itemRequestRepository.findByRequestorId(anyLong(), any())).thenReturn(List.of(itemRequest));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(itemRequestMapper.toItemRequest(itemRequestInDto)).thenReturn(itemRequest);
        when(itemRequestMapper.toDto(itemRequest)).thenReturn(itemRequestOutDto);

        assertThrows(NotFoundException.class, () -> itemRequestService.findByOwnId(1L));
    }


    @Test
    void findOtherUserItems() {
        when(itemRequestRepository.findOtherUserItems(anyLong(), any())).thenReturn(List.of(itemRequest));
        when(userRepository.findById(1L)).thenReturn(Optional.of(User.builder().build()));
        when(itemRequestMapper.toItemRequest(itemRequestInDto)).thenReturn(itemRequest);
        when(itemRequestMapper.toDto(itemRequest)).thenReturn(itemRequestOutDto);

        List<ItemRequestOutDto> list = itemRequestService.findOtherUserItems(1L, 0, 10);
        assertEquals(List.of(itemRequestOutDto), list);
    }

    @Test
    void findOtherUserItemsWithoutRequesterId() {
        when(itemRequestRepository.findOtherUserItems(anyLong(), any())).thenReturn(List.of(itemRequest));
        when(userRepository.findById(1L)).thenReturn(Optional.of(User.builder().build()));
        when(itemRequestMapper.toItemRequest(itemRequestInDto)).thenReturn(itemRequest);
        when(itemRequestMapper.toDto(itemRequest)).thenReturn(itemRequestOutDto);
        assertThrows(BadRequestException.class, () -> itemRequestService.findOtherUserItems(null, 0, 10));
    }

    @Test
    void findById() {
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(userRepository.findById(1L)).thenReturn(Optional.of(User.builder().build()));
        when(itemRequestMapper.toItemRequest(itemRequestInDto)).thenReturn(itemRequest);
        when(itemRequestMapper.toDto(itemRequest)).thenReturn(itemRequestOutDto);

        ItemRequestOutDto out = itemRequestService.findById(1L, 1L);
        assertEquals(itemRequestOutDto, out);
    }

    @Test
    void findByIdWithoutItem() {
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(User.builder().build()));
        when(itemRequestMapper.toItemRequest(itemRequestInDto)).thenReturn(itemRequest);
        when(itemRequestMapper.toDto(itemRequest)).thenReturn(itemRequestOutDto);

        assertThrows(NotFoundException.class, () -> itemRequestService.findById(1L, 1L));
    }

    @Test
    void findByIdWithoutRequestorId() {
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(userRepository.findById(1L)).thenReturn(Optional.of(User.builder().build()));
        when(itemRequestMapper.toItemRequest(itemRequestInDto)).thenReturn(itemRequest);
        when(itemRequestMapper.toDto(itemRequest)).thenReturn(itemRequestOutDto);

        assertThrows(BadRequestException.class, () -> itemRequestService.findById(null, 1L));
    }

    @Test
    void findByIdWithoutUser() {
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(itemRequestMapper.toItemRequest(itemRequestInDto)).thenReturn(itemRequest);
        when(itemRequestMapper.toDto(itemRequest)).thenReturn(itemRequestOutDto);

        assertThrows(NotFoundException.class, () -> itemRequestService.findById(1L, 1L));
    }
}