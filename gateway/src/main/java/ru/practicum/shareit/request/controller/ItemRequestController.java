package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    final ItemRequestClient client;

    @PostMapping
    public ItemRequestOutDto save(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long requestorId,
                                  @Valid @RequestBody ItemRequestInDto itemRequestInDto) {
        return client.save(requestorId, itemRequestInDto);
    }

    @GetMapping
    public List<ItemRequestOutDto> findByOwnId(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long requestorId) {
        return client.findByOwnId(requestorId);
    }

    @GetMapping("/all")
    public List<ItemRequestOutDto> findOtherUserItems(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long requestorId,
                                                      @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                      @Positive @RequestParam(defaultValue = "10") Integer size) {
        return client.findOtherUserItems(requestorId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestOutDto findById(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long requestorId,
                                      @PathVariable Long requestId) {
        return client.findById(requestorId, requestId);
    }
}
