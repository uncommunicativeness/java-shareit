package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.exceptions.BadRequestException;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BookingServiceTest {
    private final LocalDateTime start = LocalDateTime.now();
    private final LocalDateTime end = LocalDateTime.now().plusDays(1);

    private final User user = User.builder().id(1L).build();
    private final Item item = Item.builder()
            .id(1L)
            .owner(user)
            .available(true)
            .build();
    private final Booking booking = Booking.builder()
            .id(1L)
            .start(start)
            .end(end)
            .status(Booking.Status.WAITING)
            .booker(User.builder().id(2L).build())
            .item(item)
            .build();

    private final BookingInDto bookingInDto = BookingInDto.builder()
            .itemId(1L)
            .start(start)
            .end(end)
            .build();

    private final BookingOutDto bookingOutDto = BookingOutDto.builder()
            .id(1L)
            .start(start)
            .end(end)
            .status(BookingOutDto.Status.WAITING)
            .booker(BookingOutDto.Booker.builder().id(2L).build())
            .item(BookingOutDto.Item.builder().id(1L).build())
            .build();

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingMapper bookingMapper;

    @Test
    void save() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(userRepository.findById(2L)).thenReturn(Optional.ofNullable(User.builder().id(2L).build()));
        when(bookingMapper.toBooking(bookingInDto)).thenReturn(booking);
        when(bookingMapper.toDto(booking)).thenReturn(bookingOutDto);
        when(bookingRepository.save(booking)).thenReturn(booking);

        BookingOutDto out = bookingService.save(2L, bookingInDto);

        assertEquals(bookingOutDto, out);
    }

    @Test
    void saveWithoutBookerId() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(userRepository.findById(2L)).thenReturn(Optional.ofNullable(User.builder().id(2L).build()));
        when(bookingMapper.toBooking(bookingInDto)).thenReturn(booking);
        when(bookingMapper.toDto(booking)).thenReturn(bookingOutDto);
        when(bookingRepository.save(booking)).thenReturn(booking);

        assertThrows(BadRequestException.class, () -> bookingService.save(null, bookingInDto));
    }

    @Test
    void saveWithoutItem() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepository.findById(2L)).thenReturn(Optional.ofNullable(User.builder().id(2L).build()));
        when(bookingMapper.toBooking(bookingInDto)).thenReturn(booking);
        when(bookingMapper.toDto(booking)).thenReturn(bookingOutDto);
        when(bookingRepository.save(booking)).thenReturn(booking);

        assertThrows(NotFoundException.class, () -> bookingService.save(1L, bookingInDto));
    }


    @Test
    void setStatus() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(bookingMapper.toBooking(bookingInDto)).thenReturn(booking);
        when(bookingMapper.toDto(booking)).thenReturn(bookingOutDto);
        when(bookingRepository.save(booking)).thenReturn(booking);

        BookingOutDto out = bookingService.setStatus(booking.getId(), user.getId(), true);

        assertEquals(bookingOutDto, out);
    }

    @Test
    void setStatusWithoutBookingId() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(bookingMapper.toBooking(bookingInDto)).thenReturn(booking);
        when(bookingMapper.toDto(booking)).thenReturn(bookingOutDto);
        when(bookingRepository.save(booking)).thenReturn(booking);

        assertThrows(NotFoundException.class, () -> bookingService.setStatus(booking.getId(), user.getId(), true));
    }

    @Test
    void setStatusWithoutOwnerId() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(bookingMapper.toBooking(bookingInDto)).thenReturn(booking);
        when(bookingMapper.toDto(booking)).thenReturn(bookingOutDto);
        when(bookingRepository.save(booking)).thenReturn(booking);

        assertThrows(BadRequestException.class, () -> bookingService.setStatus(booking.getId(), null, true));
    }

    @Test
    void findById() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingMapper.toBooking(bookingInDto)).thenReturn(booking);
        when(bookingMapper.toDto(booking)).thenReturn(bookingOutDto);

        BookingOutDto out = bookingService.findById(1L, 1L);

        assertEquals(bookingOutDto, out);
    }

    @Test
    void findAllByBookerAndState() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingRepository.findByBooker_Id(anyLong(), any())).thenReturn(List.of(booking));
        when(bookingRepository.findByBookerCurrentBookings(anyLong(), any(), any())).thenReturn(List.of(booking));
        when(bookingRepository.findByBookerPastBookings(anyLong(), any(), any())).thenReturn(List.of(booking));
        when(bookingRepository.findByBookerFutureBookings(anyLong(), any(), any())).thenReturn(List.of(booking));
        when(bookingRepository.findByBooker_IdAndStatus(anyLong(), any(), any())).thenReturn(List.of(booking));
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findById(2L)).thenReturn(Optional.ofNullable(User.builder().id(2L).build()));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(bookingMapper.toBooking(bookingInDto)).thenReturn(booking);
        when(bookingMapper.toDto(booking)).thenReturn(bookingOutDto);

        List<BookingOutDto> listAll = bookingService
                .findAllByBookerAndState(2L, "ALL", 0, 10);

        List<BookingOutDto> listCurrent = bookingService
                .findAllByBookerAndState(2L, "CURRENT", 0, 10);

        List<BookingOutDto> listPast = bookingService
                .findAllByBookerAndState(2L, "PAST", 0, 10);

        List<BookingOutDto> listFeature = bookingService
                .findAllByBookerAndState(2L, "FUTURE", 0, 10);

        List<BookingOutDto> listWaiting = bookingService
                .findAllByBookerAndState(2L, "WAITING", 0, 10);

        List<BookingOutDto> listRejected = bookingService
                .findAllByBookerAndState(2L, "REJECTED", 0, 10);

        assertEquals(List.of(bookingOutDto), listAll);
        assertEquals(List.of(bookingOutDto), listCurrent);
        assertEquals(List.of(bookingOutDto), listPast);
        assertEquals(List.of(bookingOutDto), listFeature);
        assertEquals(List.of(bookingOutDto), listWaiting);
        assertEquals(List.of(bookingOutDto), listRejected);

        assertThrows(BadRequestException.class, () -> bookingService
                .findAllByBookerAndState(2L, "INCORRECT", 0, 10));
    }

    @Test
    void findAllByOwnerAndState() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingRepository.findByItem_Owner_Id(anyLong(), any())).thenReturn(List.of(booking));
        when(bookingRepository.findByOwnerIdCurrentBookings(anyLong(), any(), any())).thenReturn(List.of(booking));
        when(bookingRepository.findByOwnerIdPastBookings(anyLong(), any(), any())).thenReturn(List.of(booking));
        when(bookingRepository.findByOwnerIdFutureBookings(anyLong(), any(), any())).thenReturn(List.of(booking));
        when(bookingRepository.findByItem_Owner_IdAndStatus(anyLong(), any(), any())).thenReturn(List.of(booking));
        when(bookingRepository.findByItem_Owner_IdAndStatus(anyLong(), any(), any())).thenReturn(List.of(booking));
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findById(2L)).thenReturn(Optional.ofNullable(User.builder().id(2L).build()));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(bookingMapper.toBooking(bookingInDto)).thenReturn(booking);
        when(bookingMapper.toDto(booking)).thenReturn(bookingOutDto);

        List<BookingOutDto> listAll = bookingService
                .findAllByOwnerAndState(2L, "ALL", 0, 10);

        List<BookingOutDto> listCurrent = bookingService
                .findAllByOwnerAndState(2L, "CURRENT", 0, 10);

        List<BookingOutDto> listPast = bookingService
                .findAllByOwnerAndState(2L, "PAST", 0, 10);

        List<BookingOutDto> listFeature = bookingService
                .findAllByOwnerAndState(2L, "FUTURE", 0, 10);

        List<BookingOutDto> listWaiting = bookingService
                .findAllByOwnerAndState(2L, "WAITING", 0, 10);

        List<BookingOutDto> listRejected = bookingService
                .findAllByOwnerAndState(2L, "REJECTED", 0, 10);

        assertEquals(List.of(bookingOutDto), listAll);
        assertEquals(List.of(bookingOutDto), listCurrent);
        assertEquals(List.of(bookingOutDto), listPast);
        assertEquals(List.of(bookingOutDto), listFeature);
        assertEquals(List.of(bookingOutDto), listWaiting);
        assertEquals(List.of(bookingOutDto), listRejected);

        assertThrows(BadRequestException.class, () -> bookingService
                .findAllByOwnerAndState(2L, "INCORRECT", 0, 10));
    }
}