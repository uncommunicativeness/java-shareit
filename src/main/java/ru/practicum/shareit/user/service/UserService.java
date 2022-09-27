package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.exceptions.ConflictException;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserInDto;
import ru.practicum.shareit.user.dto.UserOutDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    final UserRepository userRepository;
    final UserMapper userMapper;

    public List<UserOutDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserOutDto findById(Long id) {
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            return userMapper.toDto(optional.get());
        }

        throw new NotFoundException(String.format("Пользователь с id=%d не найден", id));
    }

    @Transactional
    public UserOutDto save(UserInDto userInDto) {
        User user;
        try {
            user = userRepository.save(userMapper.toUser(userInDto));
        } catch (Exception e) {
            throw new ConflictException("Ошибка сохранения пользователя");
        }
        return userMapper.toDto(user);
    }

    @Transactional
    public UserOutDto update(Long id, UserInDto userInDto) {
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            User user = optional.get();

            if (userInDto.getName() != null) {
                user.setName(userInDto.getName());
            }
            if (userInDto.getEmail() != null) {
                user.setEmail(userInDto.getEmail());
            }

            try {
                user = userRepository.save(user);
            } catch (Exception e) {
                throw new ConflictException("Ошибка сохранения пользователя");
            }
            return userMapper.toDto(user);
        }

        throw new NotFoundException(String.format("Пользователь с id=%d не найден", id));
    }

    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
