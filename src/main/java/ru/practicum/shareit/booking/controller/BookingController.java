package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public BookingDto findById(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                               @PathVariable Long bookingId) {
        return bookingService.findById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> findAllByBookerAndState(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long bookerId,
                                                    @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.findAllByBookerAndState(bookerId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> findAllByOwnerAndState(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long ownerId,
                                                   @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.findAllByOwnerAndState(ownerId, state);
    }


    @PostMapping
    public BookingDto save(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long bookerId,
                           @Valid @RequestBody BookingDto bookingDto) {
        return bookingService.save(bookerId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto setStatus(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long ownerId,
                                @PathVariable Long bookingId,
                                @RequestParam Boolean approved) {
        return bookingService.setStatus(bookingId, ownerId, approved);
    }
}
