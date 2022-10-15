package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestOutDto save(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long requestorId,
                                  @Valid @RequestBody ItemRequestInDto itemRequestInDto) {
        return itemRequestService.save(requestorId, itemRequestInDto);
    }

    @GetMapping
    public List<ItemRequestOutDto> findByOwnId(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long requestorId) {
        return itemRequestService.findByOwnId(requestorId);
    }

    @GetMapping("/all")
    public List<ItemRequestOutDto> findOtherUserItems(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long requestorId,
                                                      @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                      @Positive @RequestParam(defaultValue = "10") Integer size) {
        return itemRequestService.findOtherUserItems(requestorId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestOutDto findById(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long requestorId,
                                      @PathVariable Long requestId) {
        return itemRequestService.findById(requestorId, requestId);
    }
}
