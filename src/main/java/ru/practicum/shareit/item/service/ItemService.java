package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.exceptions.BadRequestException;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.utils.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    final ItemRepository itemRepository;
    final UserRepository userRepository;

    final ObjectMapper mapper;

    public ItemDto findById(Long id) {
        Optional<Item> optional = itemRepository.findById(id);
        if (optional.isPresent()) {
            return mapper.itemToItemDtoMapper(optional.get());
        }

        throw new NotFoundException(String.format("Пользователь с id=%d не найден", id));
    }

    public List<ItemDto> findAllByOwnerId(Long id) {
        return itemRepository.findAllByOwnerId(id)
                .stream()
                .map(mapper::itemToItemDtoMapper)
                .collect(Collectors.toList());
    }

    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.search(text)
                .stream()
                .map(mapper::itemToItemDtoMapper)
                .collect(Collectors.toList());
    }

    public ItemDto save(Long id, ItemDto itemDto) {
        if (id == null) {
            throw new BadRequestException("В запросе не был передан заголовок X-Sharer-User-Id");
        }

        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            User owner = optional.get();
            Item item = mapper.itemDtoTOItemMapper(itemDto);
            item.setOwner(owner);
            item = itemRepository.save(item);
            return mapper.itemToItemDtoMapper(item);
        }

        throw new NotFoundException(String.format("Пользователь с id=%d не найден", id));
    }

    public ItemDto update(Long id, Long itemId, ItemDto itemDto) {
        if (id == null) {
            throw new BadRequestException("В запросе не был передан заголовок X-Sharer-User-Id");
        }

        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с id=%d не найден", id));
        }
        User owner = userOptional.get();

        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (itemOptional.isEmpty()) {
            throw new NotFoundException(String.format("Вещь с id=%d не найдена", itemId));
        }
        Item item = itemOptional.get();

        if (item.getOwner() != owner) {
            throw new NotFoundException(String.format("Вещь с id=%d не доступна для редактирования пользователю с id=%d", itemId, id));
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        item = itemRepository.save(item);

        return mapper.itemToItemDtoMapper(item);
    }

}
