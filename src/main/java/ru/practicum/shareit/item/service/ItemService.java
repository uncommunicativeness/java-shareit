package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.exceptions.BadRequestException;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDateDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.utils.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    final ItemRepository itemRepository;
    final UserRepository userRepository;
    final BookingRepository bookingRepository;
    final CommentRepository commentRepository;
    final ObjectMapper mapper;

    public ItemWithBookingDateDto findById(Long userId, Long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();

            return getItemWithBookingDateDto(userId, item);
        }

        throw new NotFoundException(String.format("Пользователь с id=%d не найден", itemId));
    }

    public List<ItemWithBookingDateDto> findAllByOwnerId(Long ownerId) {
        return itemRepository.findAllByOwnerId(ownerId)
                .stream()
                .map(item -> getItemWithBookingDateDto(ownerId, item))
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

    public ItemDto saveItem(Long id, ItemDto itemDto) {
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

    public CommentDto saveComment(Long userId, Long itemId, CommentDto commentDto) {
        if (userId == null) {
            throw new BadRequestException("В запросе не был передан заголовок X-Sharer-User-Id");
        }

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с id=%d не найден", userId));
        }
        User user = userOptional.get();

        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (itemOptional.isEmpty()) {
            throw new NotFoundException(String.format("Вещь с id=%d не найдена", itemId));
        }
        Item item = itemOptional.get();

        LocalDateTime localDateTime = LocalDateTime.now();

        if (item.getBookings().stream()
                .filter(booking -> booking.getEnd().isBefore(localDateTime))
                .map(Booking::getBooker)
                .anyMatch(booker -> booker.equals(user))) {
            Comment comment = mapper.commentDtoToCommentMapper(commentDto);
            comment.setItem(item);
            comment.setAuthor(user);
            comment = commentRepository.save(comment);
            return mapper.commentToCommentDtoMapper(comment);
        }
        throw new BadRequestException("Не удалось оставить комментарий");
    }

    private ItemWithBookingDateDto getItemWithBookingDateDto(Long userId, Item item) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с id=%d не найден", userId));
        }

        ItemWithBookingDateDto itemWithBookingDateDto = mapper.itemToItemWithBookingDateDtoMapper(item);

        if (Objects.equals(item.getOwner().getId(), userId)) {
            Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "end"));
            List<Booking> bookings = bookingRepository.getLastBookings(userId, item.getId(), pageable).getContent();
            try {
                itemWithBookingDateDto.setNextBooking(mapper.bookingToBookingDtoMapper(bookings.get(0)));
                itemWithBookingDateDto.setLastBooking(mapper.bookingToBookingDtoMapper(bookings.get(1)));
            } catch (Exception ignored) {
            }
        }
        return itemWithBookingDateDto;
    }
}
