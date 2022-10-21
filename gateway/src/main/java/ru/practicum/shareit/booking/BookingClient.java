package ru.practicum.shareit.booking;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@FeignClient(value = "bookings",
        path = "bookings",
        url = "${shareit-server.url}")
public interface BookingClient {
    @GetMapping("/{bookingId}")
    BookingOutDto findById(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                           @PathVariable Long bookingId);

    @GetMapping
    List<BookingOutDto> findAllByBookerAndState(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long bookerId,
                                                @RequestParam(defaultValue = "ALL") String state,
                                                @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                                @Positive @RequestParam(required = false, defaultValue = "10") Integer size);

    @GetMapping("/owner")
    List<BookingOutDto> findAllByOwnerAndState(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long ownerId,
                                               @RequestParam(defaultValue = "ALL") String state,
                                               @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                               @Positive @RequestParam(required = false, defaultValue = "10") Integer size);

    @PostMapping
    BookingOutDto save(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long bookerId,
                       @Valid @RequestBody BookingInDto bookingInDto);

    @PatchMapping("/{bookingId}")
    BookingOutDto setStatus(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long ownerId,
                            @PathVariable Long bookingId,
                            @RequestParam Boolean approved);
}
