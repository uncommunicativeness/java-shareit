package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.exceptions.BadRequestException;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.comment.CommentInDto;
import ru.practicum.shareit.item.dto.comment.CommentOutDto;
import ru.practicum.shareit.item.dto.item.ItemInDto;
import ru.practicum.shareit.item.dto.item.ItemOutDto;
import ru.practicum.shareit.item.dto.item.ItemWithBookingDateOutDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ItemServiceTest {
    private final User user = User.builder()
            .id(1L)
            .name("Name")
            .email("name@email")
            .build();
    private final Item item = Item.builder()
            .id(1L)
            .name("Name")
            .description("Description")
            .available(true)
            .owner(user)
            .bookings(Set.of(Booking.builder()
                    .id(1L)
                    .end(LocalDateTime.MIN)
                    .booker(user)
                    .build()))
            .comments(Set.of())
            .build();
    private final Comment comment = Comment.builder()
            .id(1L)
            .text("text")
            .author(user)
            .item(item)
            .created(LocalDateTime.now())
            .build();
    private final CommentInDto commentInDto = CommentInDto.builder().text("text").build();
    private final CommentOutDto commentOutDto = CommentOutDto.builder()
            .id(1L)
            .text("text")
            .authorName("Name")
            .created(LocalDateTime.now())
            .build();
    private final ItemInDto itemInDto = ItemInDto.builder()
            .name("Name")
            .description("Description")
            .available(true)
            .build();
    private final ItemOutDto itemOutDto = ItemOutDto.builder()
            .id(1L)
            .name("Name")
            .description("Description")
            .available(true)
            .comments(Set.of(ItemOutDto.Comment.builder()
                    .id(1L)
                    .text("text")
                    .authorName("Name")
                    .created(LocalDateTime.now())
                    .build()))
            .build();
    private final ItemWithBookingDateOutDto itemWithBookingDateOutDto = ItemWithBookingDateOutDto.builder()
            .id(1L)
            .name("Name")
            .description("Description")
            .available(true)
            .nextBooking(ItemWithBookingDateOutDto.Booking.builder()
                    .id(1L)
                    .bookerId(2L)
                    .build())
            .lastBooking(ItemWithBookingDateOutDto.Booking.builder()
                    .id(2L)
                    .bookerId(3L)
                    .build())
            .comments(Set.of(ItemWithBookingDateOutDto.Comment.builder()
                    .id(1L)
                    .text("text")
                    .authorName("Name")
                    .created(LocalDateTime.now())
                    .build()))
            .build();
    @InjectMocks
    private ItemService itemService;
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ItemMapper itemMapper;
    @Mock
    private CommentMapper commentMapper;

    @Test
    void findById() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.getLastBookings(anyLong(), anyLong(), any()))
                .thenReturn(Page.empty());
        when(itemMapper.toItem(itemInDto)).thenReturn(item);
        when(itemMapper.toIemOutDto(item)).thenReturn(itemOutDto);
        when(itemMapper.toItemWithBookingDateOutDto(any(), any(), any()))
                .thenReturn(itemWithBookingDateOutDto);

        ItemWithBookingDateOutDto out = itemService.findById(1L, item.getId());
        assertEquals(itemWithBookingDateOutDto, out);
    }

    @Test
    void findByIdWithoutItem() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.getLastBookings(anyLong(), anyLong(), any()))
                .thenReturn(Page.empty());
        when(itemMapper.toItem(itemInDto)).thenReturn(item);
        when(itemMapper.toIemOutDto(item)).thenReturn(itemOutDto);
        when(itemMapper.toItemWithBookingDateOutDto(any(), any(), any()))
                .thenReturn(itemWithBookingDateOutDto);

        assertThrows(NotFoundException.class, () -> itemService.findById(1L, item.getId()));
    }

    @Test
    void findByIdUserNotOwner() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(userRepository.findById(3L)).thenReturn(Optional.of(User.builder().id(3L).build()));
        when(bookingRepository.getLastBookings(anyLong(), anyLong(), any()))
                .thenReturn(Page.empty());
        when(itemMapper.toItem(itemInDto)).thenReturn(item);
        when(itemMapper.toIemOutDto(item)).thenReturn(itemOutDto);
        when(itemMapper.toItemWithBookingDateOutDto(any(), any(), any()))
                .thenReturn(itemWithBookingDateOutDto);

        ItemWithBookingDateOutDto out = itemService.findById(3L, 1L);
        assertEquals(itemWithBookingDateOutDto, out);
    }

    @Test
    void findByIdWithoutUser() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.getLastBookings(anyLong(), anyLong(), any()))
                .thenReturn(Page.empty());
        when(itemMapper.toItem(itemInDto)).thenReturn(item);
        when(itemMapper.toIemOutDto(item)).thenReturn(itemOutDto);
        when(itemMapper.toItemWithBookingDateOutDto(any(), any(), any()))
                .thenReturn(itemWithBookingDateOutDto);

        assertThrows(NotFoundException.class, () -> itemService.findById(null, 1L));
    }

    @Test
    void findAllByOwnerId() {
        when(itemRepository.findAllByOwnerId(anyLong(), any())).thenReturn(List.of(item));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.getLastBookings(anyLong(), anyLong(), any()))
                .thenReturn(Page.empty());
        when(itemMapper.toItem(itemInDto)).thenReturn(item);
        when(itemMapper.toIemOutDto(item)).thenReturn(itemOutDto);
        when(itemMapper.toItemWithBookingDateOutDto(any(), any(), any()))
                .thenReturn(itemWithBookingDateOutDto);

        List<ItemWithBookingDateOutDto> list = itemService.findAllByOwnerId(1L, 0, 10);
        assertEquals(list.size(), 1);
        assertEquals(list.get(0), itemWithBookingDateOutDto);
    }

    @Test
    void search() {
        when(itemRepository.search(anyString(), any())).thenReturn(List.of(item));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.getLastBookings(anyLong(), anyLong(), any()))
                .thenReturn(Page.empty());
        when(itemMapper.toItem(itemInDto)).thenReturn(item);
        when(itemMapper.toIemOutDto(item)).thenReturn(itemOutDto);
        when(itemMapper.toItemWithBookingDateOutDto(any(), any(), any()))
                .thenReturn(itemWithBookingDateOutDto);

        List<ItemOutDto> list = itemService.search("text", 0, 10);
        assertEquals(list.size(), 1);
        assertEquals(list.get(0), itemOutDto);
    }

    @Test
    void searchWithoutText() {
        when(itemRepository.search(anyString(), any())).thenReturn(List.of(item));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.getLastBookings(anyLong(), anyLong(), any()))
                .thenReturn(Page.empty());
        when(itemMapper.toItem(itemInDto)).thenReturn(item);
        when(itemMapper.toIemOutDto(item)).thenReturn(itemOutDto);
        when(itemMapper.toItemWithBookingDateOutDto(any(), any(), any()))
                .thenReturn(itemWithBookingDateOutDto);

        List<ItemOutDto> list = itemService.search("", 0, 10);
        assertEquals(list.size(), 0);
    }

    @Test
    void saveItem() {
        when(itemRepository.save(item)).thenReturn(item);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.getLastBookings(anyLong(), anyLong(), any()))
                .thenReturn(Page.empty());
        when(itemMapper.toItem(itemInDto)).thenReturn(item);
        when(itemMapper.toIemOutDto(item)).thenReturn(itemOutDto);
        when(itemMapper.toItemWithBookingDateOutDto(any(), any(), any()))
                .thenReturn(itemWithBookingDateOutDto);

        ItemOutDto out = itemService.saveItem(1L, itemInDto);
        assertEquals(out, itemOutDto);
    }

    @Test
    void saveItemWithoutUser() {
        when(itemRepository.save(item)).thenReturn(item);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(bookingRepository.getLastBookings(anyLong(), anyLong(), any()))
                .thenReturn(Page.empty());
        when(itemMapper.toItem(itemInDto)).thenReturn(item);
        when(itemMapper.toIemOutDto(item)).thenReturn(itemOutDto);
        when(itemMapper.toItemWithBookingDateOutDto(any(), any(), any()))
                .thenReturn(itemWithBookingDateOutDto);

        assertThrows(NotFoundException.class, () -> itemService.saveItem(1L, itemInDto));
    }

    @Test
    void saveItemWithoutId() {
        when(itemRepository.save(item)).thenReturn(item);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(ItemRequest.builder().id(1L).build()));
        when(bookingRepository.getLastBookings(anyLong(), anyLong(), any()))
                .thenReturn(Page.empty());
        when(itemMapper.toItem(itemInDto)).thenReturn(item);
        when(itemMapper.toIemOutDto(item)).thenReturn(itemOutDto);
        when(itemMapper.toItemWithBookingDateOutDto(any(), any(), any()))
                .thenReturn(itemWithBookingDateOutDto);

        assertThrows(BadRequestException.class, () -> itemService.saveItem(null, itemInDto));
    }

    @Test
    void saveItemWithRequestId() {
        when(itemRepository.save(item)).thenReturn(item);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(ItemRequest.builder().id(1L).build()));
        when(bookingRepository.getLastBookings(anyLong(), anyLong(), any()))
                .thenReturn(Page.empty());
        when(itemMapper.toItem(any())).thenReturn(item);
        when(itemMapper.toIemOutDto(item)).thenReturn(itemOutDto);
        when(itemMapper.toItemWithBookingDateOutDto(any(), any(), any()))
                .thenReturn(itemWithBookingDateOutDto);

        ItemOutDto out = itemService.saveItem(1L, ItemInDto.builder()
                .name("Name")
                .description("Description")
                .available(true)
                .requestId(1L)
                .build());
        assertEquals(out, itemOutDto);
    }

    @Test
    void update() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.getLastBookings(anyLong(), anyLong(), any()))
                .thenReturn(Page.empty());
        when(itemMapper.toItem(itemInDto)).thenReturn(item);
        when(itemMapper.toIemOutDto(item)).thenReturn(itemOutDto);
        when(itemMapper.toItemWithBookingDateOutDto(any(), any(), any()))
                .thenReturn(itemWithBookingDateOutDto);

        ItemOutDto out = itemService.update(1L, 1L, itemInDto);
        assertEquals(out, itemOutDto);
    }

    @Test
    void updateWithoutId() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.getLastBookings(anyLong(), anyLong(), any()))
                .thenReturn(Page.empty());
        when(itemMapper.toItem(itemInDto)).thenReturn(item);
        when(itemMapper.toIemOutDto(item)).thenReturn(itemOutDto);
        when(itemMapper.toItemWithBookingDateOutDto(any(), any(), any()))
                .thenReturn(itemWithBookingDateOutDto);

        assertThrows(BadRequestException.class, () -> itemService.update(null, 1L, itemInDto));
    }

    @Test
    void updateWithoutItem() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(itemRepository.save(item)).thenReturn(item);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.getLastBookings(anyLong(), anyLong(), any()))
                .thenReturn(Page.empty());
        when(itemMapper.toItem(itemInDto)).thenReturn(item);
        when(itemMapper.toIemOutDto(item)).thenReturn(itemOutDto);
        when(itemMapper.toItemWithBookingDateOutDto(any(), any(), any()))
                .thenReturn(itemWithBookingDateOutDto);

        assertThrows(NotFoundException.class, () -> itemService.update(1L, 1L, itemInDto));
    }

    @Test
    void updateWithoutUser() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(bookingRepository.getLastBookings(anyLong(), anyLong(), any()))
                .thenReturn(Page.empty());
        when(itemMapper.toItem(itemInDto)).thenReturn(item);
        when(itemMapper.toIemOutDto(item)).thenReturn(itemOutDto);
        when(itemMapper.toItemWithBookingDateOutDto(any(), any(), any()))
                .thenReturn(itemWithBookingDateOutDto);

        assertThrows(NotFoundException.class, () -> itemService.update(1L, 1L, itemInDto));
    }

    @Test
    void updateUserNotOwner() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);
        when(userRepository.findById(1L)).thenReturn(Optional.of(User.builder().id(2L).build()));
        when(bookingRepository.getLastBookings(anyLong(), anyLong(), any()))
                .thenReturn(Page.empty());
        when(itemMapper.toItem(itemInDto)).thenReturn(item);
        when(itemMapper.toIemOutDto(item)).thenReturn(itemOutDto);
        when(itemMapper.toItemWithBookingDateOutDto(any(), any(), any()))
                .thenReturn(itemWithBookingDateOutDto);

        assertThrows(NotFoundException.class, () -> itemService.update(1L, 1L, itemInDto));
    }

    @Test
    void saveComment() {
        when(commentRepository.save(comment)).thenReturn(comment);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.getLastBookings(anyLong(), anyLong(), any()))
                .thenReturn(Page.empty());
        when(itemMapper.toItem(itemInDto)).thenReturn(item);
        when(itemMapper.toIemOutDto(item)).thenReturn(itemOutDto);
        when(commentMapper.toComment(commentInDto)).thenReturn(comment);
        when(commentMapper.toDto(comment)).thenReturn(commentOutDto);
        when(itemMapper.toItemWithBookingDateOutDto(any(), any(), any()))
                .thenReturn(itemWithBookingDateOutDto);

        CommentOutDto out = itemService.saveComment(1L, 1L, commentInDto);
        assertEquals(out, commentOutDto);
    }

    @Test
    void saveCommentWithoutUserId() {
        when(commentRepository.save(comment)).thenReturn(comment);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.getLastBookings(anyLong(), anyLong(), any()))
                .thenReturn(Page.empty());
        when(itemMapper.toItem(itemInDto)).thenReturn(item);
        when(itemMapper.toIemOutDto(item)).thenReturn(itemOutDto);
        when(commentMapper.toComment(commentInDto)).thenReturn(comment);
        when(commentMapper.toDto(comment)).thenReturn(commentOutDto);
        when(itemMapper.toItemWithBookingDateOutDto(any(), any(), any()))
                .thenReturn(itemWithBookingDateOutDto);

        assertThrows(BadRequestException.class, () -> itemService.saveComment(null, 1L, commentInDto));
    }

    @Test
    void saveCommentWithoutUser() {
        when(commentRepository.save(comment)).thenReturn(comment);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(bookingRepository.getLastBookings(anyLong(), anyLong(), any()))
                .thenReturn(Page.empty());
        when(itemMapper.toItem(itemInDto)).thenReturn(item);
        when(itemMapper.toIemOutDto(item)).thenReturn(itemOutDto);
        when(commentMapper.toComment(commentInDto)).thenReturn(comment);
        when(commentMapper.toDto(comment)).thenReturn(commentOutDto);
        when(itemMapper.toItemWithBookingDateOutDto(any(), any(), any()))
                .thenReturn(itemWithBookingDateOutDto);

        assertThrows(NotFoundException.class, () -> itemService.saveComment(1L, 1L, commentInDto));
    }

    @Test
    void saveCommentWithoutItem() {
        when(commentRepository.save(comment)).thenReturn(comment);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.getLastBookings(anyLong(), anyLong(), any()))
                .thenReturn(Page.empty());
        when(itemMapper.toItem(itemInDto)).thenReturn(item);
        when(itemMapper.toIemOutDto(item)).thenReturn(itemOutDto);
        when(commentMapper.toComment(commentInDto)).thenReturn(comment);
        when(commentMapper.toDto(comment)).thenReturn(commentOutDto);
        when(itemMapper.toItemWithBookingDateOutDto(any(), any(), any()))
                .thenReturn(itemWithBookingDateOutDto);

        assertThrows(NotFoundException.class, () -> itemService.saveComment(1L, 1L, commentInDto));
    }
}