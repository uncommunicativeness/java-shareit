package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.comment.CommentInDto;
import ru.practicum.shareit.item.dto.comment.CommentOutDto;
import ru.practicum.shareit.item.dto.item.ItemInDto;
import ru.practicum.shareit.item.dto.item.ItemOutDto;
import ru.practicum.shareit.item.dto.item.ItemWithBookingDateOutDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    final ItemService itemService;

    @GetMapping
    public List<ItemWithBookingDateOutDto> findAllByOwnerId(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long ownerId,
                                                            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                                            @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        return itemService.findAllByOwnerId(ownerId, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemWithBookingDateOutDto findById(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                                              @PathVariable Long itemId) {
        return itemService.findById(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemOutDto> search(@RequestParam String text,
                                   @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                   @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        return itemService.search(text, from, size);
    }

    @PostMapping
    public ItemOutDto saveItem(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long id,
                               @Valid @RequestBody ItemInDto itemInDto) {
        return itemService.saveItem(id, itemInDto);
    }

    @PostMapping("{itemId}/comment")
    public CommentOutDto saveComment(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                                     @PathVariable Long itemId,
                                     @Valid @RequestBody CommentInDto commentInDto) {
        return itemService.saveComment(userId, itemId, commentInDto);
    }

    @PatchMapping("/{itemId}")
    public ItemOutDto update(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long id,
                             @PathVariable Long itemId,
                             @RequestBody ItemInDto itemInDto) {
        return itemService.update(id, itemId, itemInDto);
    }
}
