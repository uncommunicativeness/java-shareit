package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.exceptions.ConflictException;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.utils.ObjectMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    final UserRepository userRepository;
    final ObjectMapper mapper;

    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(mapper::userToUserDtoMapper)
                .collect(Collectors.toList());
    }

    public UserDto findById(Long id) {
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            return mapper.userToUserDtoMapper(optional.get());
        }

        throw new NotFoundException(String.format("Пользователь с id=%d не найден", id));
    }

    public UserDto save(UserDto userDto) {
        // На данный момент при проверке уникальности email
        // выполняем в сервисе, т.к. иначе будет сдвиг идентификатора
        // и последующие тесты не пройдут
        // TODO Удалить в следующей итерации
        Optional<User> optional = userRepository.findByEmail(userDto.getEmail());
        if (optional.isPresent()) {
            throw new ConflictException(String.format("Пользователь с email=%s уже зарегистрирован", userDto.getEmail()));
        }

        User user;
        try {
            user = userRepository.save(mapper.userDtoToUserMapper(userDto));
        } catch (Exception e) {
            throw new ConflictException("Ошибка сохранения пользователя");
        }
        return mapper.userToUserDtoMapper(user);
    }

    public UserDto update(Long id, UserDto userDto) {
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            User user = optional.get();

            if (userDto.getName() != null) {
                user.setName(userDto.getName());
            }
            if (userDto.getEmail() != null) {
                user.setEmail(userDto.getEmail());
            }

            try {
                user = userRepository.save(user);
            } catch (Exception e) {
                throw new ConflictException("Ошибка сохранения пользователя");
            }
            return mapper.userToUserDtoMapper(userRepository.save(user));
        }

        throw new NotFoundException(String.format("Пользователь с id=%d не найден", id));
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
