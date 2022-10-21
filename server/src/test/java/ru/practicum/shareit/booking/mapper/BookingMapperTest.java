package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class BookingMapperTest {
    private final LocalDateTime start = LocalDateTime.now();

    private final Booking booking = Booking.builder()
            .id(1L)
            .start(start)
            .booker(User.builder().build())
            .item(Item.builder().build())
            .build();

    private final BookingInDto bookingInDto = BookingInDto.builder()
            .start(start)
            .build();
    private final BookingOutDto bookingOutDto = BookingOutDto.builder()
            .id(1L)
            .start(start)
            .build();
    @InjectMocks
    private BookingMapper mapper;

    @Test
    void toBooking() {
        assertEquals(booking.getStart(), mapper.toBooking(bookingInDto).getStart());
    }

    @Test
    void toDto() {
        assertEquals(bookingOutDto.getId(), mapper.toDto(booking).getId());
    }
}