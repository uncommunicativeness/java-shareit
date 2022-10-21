package ru.practicum.shareit.request;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@FeignClient(value = "requests",
        path = "requests",
        url = "${shareit-server.url}")
public interface ItemRequestClient {
    @PostMapping
    ItemRequestOutDto save(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long requestorId,
                           @Valid @RequestBody ItemRequestInDto itemRequestInDto);

    @GetMapping
    List<ItemRequestOutDto> findByOwnId(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long requestorId);

    @GetMapping("/all")
    List<ItemRequestOutDto> findOtherUserItems(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long requestorId,
                                               @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                               @Positive @RequestParam(defaultValue = "10") Integer size);

    @GetMapping("/{requestId}")
    ItemRequestOutDto findById(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long requestorId,
                               @PathVariable Long requestId);
}
