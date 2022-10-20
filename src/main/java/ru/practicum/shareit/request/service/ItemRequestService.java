package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.exceptions.BadRequestException;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestService {
    final ItemRequestRepository itemRequestRepository;
    final ItemRequestMapper mapper;
    final UserRepository userRepository;

    @Transactional
    public ItemRequestOutDto save(Long requestorId, ItemRequestInDto itemRequestInDto) {
        if (requestorId == null) {
            throw new BadRequestException("В запросе не был передан заголовок X-Sharer-User-Id");
        }
        Optional<User> optional = userRepository.findById(requestorId);
        if (optional.isPresent()) {
            User requestor = optional.get();
            ItemRequest itemRequest = mapper.toItemRequest(itemRequestInDto);
            itemRequest.setRequestor(requestor);
            itemRequest = itemRequestRepository.save(itemRequest);

            return mapper.toDto(itemRequest);
        }

        throw new NotFoundException(String.format("Пользователь с id=%d не найден", requestorId));
    }

    public List<ItemRequestOutDto> findByOwnId(Long requestorId) {
        if (requestorId == null) {
            throw new BadRequestException("В запросе не был передан заголовок X-Sharer-User-Id");
        }
        Optional<User> optional = userRepository.findById(requestorId);
        if (optional.isPresent()) {
            List<ItemRequest> itemRequests = itemRequestRepository
                    .findByRequestorId(requestorId, Sort.by("created").descending());

            return itemRequests.stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());
        }

        throw new NotFoundException(String.format("Пользователь с id=%d не найден", requestorId));
    }

    public List<ItemRequestOutDto> findOtherUserItems(Long requestorId, Integer from, Integer size) {
        if (requestorId == null) {
            throw new BadRequestException("В запросе не был передан заголовок X-Sharer-User-Id");
        }

        List<ItemRequest> itemRequests = itemRequestRepository
                .findOtherUserItems(requestorId, PageRequest.of(from / size, size, Sort.by("created").descending()));

        return itemRequests.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public ItemRequestOutDto findById(Long requestorId, Long requestId) {
        if (requestorId == null) {
            throw new BadRequestException("В запросе не был передан заголовок X-Sharer-User-Id");
        }
        userRepository.findById(requestorId).orElseThrow(() -> new NotFoundException(String.format("Пользователь с id=%d не найден", requestorId)));

        Optional<ItemRequest> optional = itemRequestRepository.findById(requestId);
        if (optional.isPresent()) {
            ItemRequest itemRequest = optional.get();
            return mapper.toDto(itemRequest);
        }

        throw new NotFoundException(String.format("Запрос с id=%d не найден", requestId));
    }
}
