package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDateDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    final ItemService itemService;

    @GetMapping
    public List<ItemWithBookingDateDto> findAllByOwnerId(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long ownerId) {
        return itemService.findAllByOwnerId(ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemWithBookingDateDto findById(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                                           @PathVariable Long itemId) {
        return itemService.findById(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return itemService.search(text);
    }

    @PostMapping
    public ItemDto saveItem(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long id,
                            @Valid @RequestBody ItemDto itemDto) {
        return itemService.saveItem(id, itemDto);
    }

    @PostMapping("{itemId}/comment")
    public CommentDto saveComment(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                                  @PathVariable Long itemId,
                                  @Valid @RequestBody CommentDto commentDto) {
        return itemService.saveComment(userId, itemId, commentDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long id,
                          @PathVariable Long itemId,
                          @RequestBody ItemDto itemDto) {
        return itemService.update(id, itemId, itemDto);
    }
}
