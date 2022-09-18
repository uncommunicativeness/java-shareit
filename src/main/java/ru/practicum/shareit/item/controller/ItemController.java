package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    final ItemService itemService;

    @GetMapping
    public List<ItemDto> findAllByOwnerId(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long id) {
        return itemService.findAllByOwnerId(id);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable Long itemId) {
        return itemService.findById(itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return itemService.search(text);
    }

    @PostMapping
    public ItemDto save(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long id, @Valid @RequestBody ItemDto itemDto) {
        return itemService.save(id, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long id, @PathVariable Long itemId, @RequestBody ItemDto itemDto) {
        return itemService.update(id, itemId, itemDto);
    }
}
