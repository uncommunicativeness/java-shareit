package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.Booking;

@Component
public class BookingMapper {
    public Booking toBooking(BookingInDto dto) {
        return Booking.builder()
                .start(dto.getStart())
                .end(dto.getEnd())
                .build();
    }

    public BookingOutDto toDto(Booking booking) {
        return BookingOutDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .booker(BookingOutDto.Booker.builder()
                        .id(booking.getBooker().getId())
                        .build())
                .item(BookingOutDto.Item.builder()
                        .id(booking.getItem().getId())
                        .name(booking.getItem().getName())
                        .build())
                .build();
    }
}
