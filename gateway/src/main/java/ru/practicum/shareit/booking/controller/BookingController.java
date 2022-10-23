package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    final BookingClient client;

    @GetMapping("/{bookingId}")
    public BookingOutDto findById(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                                  @PathVariable Long bookingId) {
        return client.findById(userId, bookingId);
    }

    @GetMapping
    public List<BookingOutDto> findAllByBookerAndState(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long bookerId,
                                                       @RequestParam(defaultValue = "ALL") String state,
                                                       @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                                       @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        return client.findAllByBookerAndState(bookerId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingOutDto> findAllByOwnerAndState(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long ownerId,
                                                      @RequestParam(defaultValue = "ALL") String state,
                                                      @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                                      @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        return client.findAllByOwnerAndState(ownerId, state, from, size);
    }

    @PostMapping
    public BookingOutDto save(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long bookerId,
                              @Valid @RequestBody BookingInDto bookingInDto) {
        return client.save(bookerId, bookingInDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingOutDto setStatus(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long ownerId,
                                   @PathVariable Long bookingId,
                                   @RequestParam Boolean approved) {
        return client.setStatus(ownerId, bookingId, approved);
    }
}
