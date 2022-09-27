package ru.practicum.shareit.booking.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.exceptions.BadRequestException;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
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
public class BookingService {
    final BookingRepository bookingRepository;
    final UserRepository userRepository;
    final ItemRepository itemRepository;
    final BookingMapper bookingMapper;

    @Transactional
    public BookingOutDto save(Long bookerId, BookingInDto bookingInDto) {
        if (bookerId == null) {
            throw new BadRequestException("В запросе не был передан заголовок X-Sharer-User-Id");
        }

        Optional<Item> optionalItem = itemRepository.findById(bookingInDto.getItemId());
        if (optionalItem.isEmpty()) {
            throw new NotFoundException(String.format("Вещь с id=%d не найдена", bookingInDto.getItemId()));
        }

        Item item = optionalItem.get();

        if (Objects.equals(item.getOwner().getId(), bookerId)) {
            throw new NotFoundException("Владелец не может бронировать собственные вещи");
        }

        Optional<User> optionalBooker = userRepository.findById(bookerId);
        if (optionalBooker.isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с id=%d не найден", bookerId));
        }

        User booker = optionalBooker.get();

        if (!item.getAvailable()) {
            throw new BadRequestException(String.format("Вещь с id=%d недоступна для бронирования", bookingInDto.getItemId()));
        }

        Booking booking = bookingMapper.toBooking(bookingInDto);
        booking.setBooker(booker);
        booking.setItem(item);

        booking = bookingRepository.save(booking);

        return bookingMapper.toDto(booking);
    }

    @Transactional
    public BookingOutDto setStatus(Long bookingId, Long ownerId, Boolean approved) {
        if (ownerId == null) {
            throw new BadRequestException("В запросе не был передан заголовок X-Sharer-User-Id");
        }

        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            throw new NotFoundException(String.format("Бронирование с id=%d не найдена", bookingId));
        }

        Booking booking = optionalBooking.get();

        if (booking.getStatus() == Booking.Status.APPROVED) {
            throw new BadRequestException(String.format("Невозможно изменить статус бронирования с id=%d",
                    booking.getId()));
        }

        Optional<User> optionalOwner = userRepository.findById(ownerId);
        if (optionalOwner.isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с id=%d не найден", ownerId));
        }
        User owner = optionalOwner.get();

        if (booking.getItem().getOwner() != owner) {
            throw new NotFoundException(String.format("У пользователя с id=%d отсутствуют права на изменение статуса бронирования с id=%d",
                    ownerId, bookingId));
        }

        booking.setStatus(approved ? Booking.Status.APPROVED : Booking.Status.REJECTED);
        booking = bookingRepository.save(booking);

        return bookingMapper.toDto(booking);
    }

    public BookingOutDto findById(Long userId, Long bookingId) {
        if (userId == null) {
            throw new BadRequestException("В запросе не был передан заголовок X-Sharer-User-Id");
        }

        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            throw new NotFoundException(String.format("Бронирование с id=%d не найдена", bookingId));
        }
        Booking booking = optionalBooking.get();

        if (!Objects.equals(booking.getBooker().getId(), userId) && !Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new NotFoundException(String.format("У пользователя с id=%d отсутствуют права на просмотр бронирования с id=%d",
                    userId, bookingId));
        }

        return bookingMapper.toDto(booking);
    }

    public List<BookingOutDto> findAllByBookerAndState(Long bookerId, String stateString) {
        if (bookerId == null) {
            throw new BadRequestException("В запросе не был передан заголовок X-Sharer-User-Id");
        }

        State state;
        try {
            state = State.valueOf(stateString);
        } catch (Exception e) {
            throw new BadRequestException(String.format("Unknown state: %s", stateString));
        }

        Optional<User> optionalBooker = userRepository.findById(bookerId);
        if (optionalBooker.isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с id=%d не найден", bookerId));
        }

        List<Booking> bookings = new ArrayList<>();

        switch (state) {
            case ALL:
                bookings = bookingRepository.findByBooker_IdOrderByEndDesc(bookerId);
                break;
            case CURRENT:
                bookings = bookingRepository.findByBookerCurrentBookings(bookerId, LocalDateTime.now());
                break;
            case PAST:
                bookings = bookingRepository.findByBookerPastBookings(bookerId, LocalDateTime.now());
                break;
            case FUTURE:
                bookings = bookingRepository.findByBookerFutureBookings(bookerId, LocalDateTime.now());
                break;
            case WAITING:
                bookings = bookingRepository.findByBooker_IdAndStatusOrderByEndDesc(bookerId, Booking.Status.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findByBooker_IdAndStatusOrderByEndDesc(bookerId, Booking.Status.REJECTED);
                break;
        }

        return bookings.stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BookingOutDto> findAllByOwnerAndState(Long ownerId, String stateString) {
        if (ownerId == null) {
            throw new BadRequestException("В запросе не был передан заголовок X-Sharer-User-Id");
        }

        State state;
        try {
            state = State.valueOf(stateString);
        } catch (Exception e) {
            throw new BadRequestException(String.format("Unknown state: %s", stateString));
        }

        Optional<User> optionalOwner = userRepository.findById(ownerId);
        if (optionalOwner.isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с id=%d не найден", ownerId));
        }

        List<Booking> bookings;

        switch (state) {
            case ALL:
                bookings = bookingRepository.findByItem_Owner_IdOrderByEndDesc(ownerId);
                break;
            case CURRENT:
                bookings = bookingRepository.findByOwnerIdCurrentBookings(ownerId, LocalDateTime.now());
                break;
            case PAST:
                bookings = bookingRepository.findByOwnerIdPastBookings(ownerId, LocalDateTime.now());
                break;
            case FUTURE:
                bookings = bookingRepository.findByOwnerIdFutureBookings(ownerId, LocalDateTime.now());
                break;
            case WAITING:
                bookings = bookingRepository.findByItem_Owner_IdAndStatusOrderByEndDesc(ownerId, Booking.Status.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findByItem_Owner_IdAndStatusOrderByEndDesc(ownerId, Booking.Status.REJECTED);
                break;
            default:
                throw new BadRequestException("В запросе был передан некорректный статус");
        }

        return bookings.stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }
}
