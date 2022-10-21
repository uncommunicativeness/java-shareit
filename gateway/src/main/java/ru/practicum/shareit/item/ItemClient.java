package ru.practicum.shareit.item;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.comment.CommentInDto;
import ru.practicum.shareit.item.dto.comment.CommentOutDto;
import ru.practicum.shareit.item.dto.item.ItemInDto;
import ru.practicum.shareit.item.dto.item.ItemOutDto;
import ru.practicum.shareit.item.dto.item.ItemWithBookingDateOutDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@FeignClient(value = "items",
        path = "items",
        url = "${shareit-server.url}")
public interface ItemClient {
    @GetMapping
    List<ItemWithBookingDateOutDto> findAllByOwnerId(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long ownerId,
                                                     @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                                     @Positive @RequestParam(required = false, defaultValue = "10") Integer size);

    @GetMapping("/{itemId}")
    ItemWithBookingDateOutDto findById(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                                       @PathVariable Long itemId);

    @GetMapping("/search")
    List<ItemOutDto> search(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long ownerId,
                            @RequestParam String text,
                            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                            @Positive @RequestParam(required = false, defaultValue = "10") Integer size);

    @PostMapping
    ItemOutDto saveItem(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long id,
                        @Valid @RequestBody ItemInDto itemInDto);

    @PostMapping("{itemId}/comment")
    CommentOutDto saveComment(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                              @PathVariable Long itemId,
                              @Valid @RequestBody CommentInDto commentInDto);

    @PatchMapping("/{itemId}")
    ItemOutDto update(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long id,
                      @PathVariable Long itemId,
                      @RequestBody ItemInDto itemInDto);
}
