package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.comment.CommentInDto;
import ru.practicum.shareit.item.dto.comment.CommentOutDto;
import ru.practicum.shareit.item.dto.item.ItemInDto;
import ru.practicum.shareit.item.dto.item.ItemOutDto;
import ru.practicum.shareit.item.dto.item.ItemWithBookingDateOutDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    final ItemService itemService;

    @GetMapping
    public List<ItemWithBookingDateOutDto> findAllByOwnerId(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long ownerId) {
        return itemService.findAllByOwnerId(ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemWithBookingDateOutDto findById(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                                              @PathVariable Long itemId) {
        return itemService.findById(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemOutDto> search(@RequestParam String text) {
        return itemService.search(text);
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
