package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.exceptions.BadRequestException;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.comment.CommentInDto;
import ru.practicum.shareit.item.dto.comment.CommentOutDto;
import ru.practicum.shareit.item.dto.item.ItemInDto;
import ru.practicum.shareit.item.dto.item.ItemOutDto;
import ru.practicum.shareit.item.dto.item.ItemWithBookingDateOutDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {
    final ItemRepository itemRepository;
    final UserRepository userRepository;
    final BookingRepository bookingRepository;
    final CommentRepository commentRepository;
    final ItemRequestRepository itemRequestRepository;
    final ItemMapper itemMapper;
    final CommentMapper commentMapper;

    public ItemWithBookingDateOutDto findById(Long userId, Long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();

            return getItemWithBookingDateDto(userId, item);
        }

        throw new NotFoundException(String.format("Пользователь с id=%d не найден", itemId));
    }

    public List<ItemWithBookingDateOutDto> findAllByOwnerId(Long ownerId, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by("id"));

        return itemRepository.findAllByOwnerId(ownerId, pageRequest)
                .stream()
                .map(item -> getItemWithBookingDateDto(ownerId, item))
                .collect(Collectors.toList());
    }

    public List<ItemOutDto> search(String text, Integer from, Integer size) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }

        PageRequest pageRequest = PageRequest.of(from / size, size);
        return itemRepository.search(text, pageRequest)
                .stream()
                .map(itemMapper::toIemOutDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ItemOutDto saveItem(Long id, ItemInDto itemInDto) {
        if (id == null) {
            throw new BadRequestException("В запросе не был передан заголовок X-Sharer-User-Id");
        }

        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            User owner = optional.get();
            Item item = itemMapper.toItem(itemInDto);
            item.setOwner(owner);
            if (itemInDto.getRequestId() != null) {
                Optional<ItemRequest> optionalItemRequest = itemRequestRepository.findById(itemInDto.getRequestId());
                if (optionalItemRequest.isPresent()) {
                    item.setRequest(optionalItemRequest.get());
                } else {
                    throw new NotFoundException(String.format("Запрос с id=%d не найден", itemInDto.getRequestId()));
                }
            }
            item = itemRepository.save(item);
            return itemMapper.toIemOutDto(item);
        }

        throw new NotFoundException(String.format("Пользователь с id=%d не найден", id));
    }

    @Transactional
    public ItemOutDto update(Long id, Long itemId, ItemInDto itemInDto) {
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

        if (itemInDto.getName() != null) {
            item.setName(itemInDto.getName());
        }

        if (itemInDto.getDescription() != null) {
            item.setDescription(itemInDto.getDescription());
        }

        if (itemInDto.getAvailable() != null) {
            item.setAvailable(itemInDto.getAvailable());
        }

        item = itemRepository.save(item);

        return itemMapper.toIemOutDto(item);
    }

    @Transactional
    public CommentOutDto saveComment(Long userId, Long itemId, CommentInDto commentInDto) {
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
            Comment comment = commentMapper.toComment(commentInDto);
            comment.setItem(item);
            comment.setAuthor(user);
            comment = commentRepository.save(comment);
            return commentMapper.toDto(comment);
        }
        throw new BadRequestException("Не удалось оставить комментарий");
    }

    private ItemWithBookingDateOutDto getItemWithBookingDateDto(Long userId, Item item) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с id=%d не найден", userId));
        }

        ItemWithBookingDateOutDto itemWithBookingDateOutDto;
        if (Objects.equals(item.getOwner().getId(), userId)) {
            Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "end"));
            List<Booking> bookings = bookingRepository.getLastBookings(userId, item.getId(), pageable).getContent();
            Booking last = null;
            Booking next = null;
            try {
                last = bookings.get(0);
                next = bookings.get(1);
            } catch (Exception ignored) {
            }

            itemWithBookingDateOutDto = itemMapper.toItemWithBookingDateOutDto(item,
                    last == null ? null : ItemWithBookingDateOutDto.Booking.builder().id(last.getId()).bookerId(last.getBooker().getId()).build(),
                    next == null ? null : ItemWithBookingDateOutDto.Booking.builder().id(next.getId()).bookerId(next.getBooker().getId()).build()
            );
            return itemWithBookingDateOutDto;
        } else {
            return itemMapper.toItemWithBookingDateOutDto(item, null, null);
        }
    }
}
