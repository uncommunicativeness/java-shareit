package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserInDto;
import ru.practicum.shareit.user.dto.UserOutDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {

    private final UserInDto userInDto = UserInDto.builder()
            .name("Name")
            .email("name@email")
            .build();

    private final UserInDto updateInDto = UserInDto.builder()
            .name("Update Name")
            .email("name@email")
            .build();

    private final UserOutDto userOutDto = UserOutDto.builder()
            .id(1L)
            .name("Name")
            .email("name@email")
            .build();

    private final UserOutDto updateOutDto = UserOutDto.builder()
            .id(1L)
            .name("Update Name")
            .email("name@email")
            .build();
    private final User user = User.builder()
            .id(1L)
            .name("Name")
            .email("name@email")
            .build();

    private final User updateUser = User.builder()
            .id(1L)
            .name("Update Name")
            .email("name@email")
            .build();
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserService userService;


    @Test
    void save() {
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUser(userInDto)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userOutDto);

        UserOutDto out = userService.save(userInDto);
        assertEquals(userOutDto, out);
    }

    @Test
    void findById() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userMapper.toUser(userInDto)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userOutDto);

        UserOutDto out = userService.findById(user.getId());
        assertEquals(userOutDto, out);
    }

    @Test
    void findAll() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toUser(userInDto)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userOutDto);

        List<UserOutDto> list = userService.findAll();
        assertEquals(List.of(userOutDto), list);
    }

    @Test
    void update() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(updateUser));
        when(userRepository.save(user)).thenReturn(updateUser);
        when(userMapper.toUser(userInDto)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(updateOutDto);

        UserOutDto out = userService.update(user.getId(), updateInDto);

        assertEquals(updateOutDto, out);
    }

    @Test
    void delete() {
        userService.deleteById(user.getId());
        Mockito.verify(userRepository, times(1)).deleteById(user.getId());
    }

    @Test
    void updateNonExistentUser() {
        when(userRepository.findById(-1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.update(-1L, updateInDto));
    }

    @Test
    void updateUserWithNullFields() {
        when(userMapper.toUser(userInDto)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userOutDto);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        UserOutDto out = userService.update(user.getId(), UserInDto.builder().build());

        assertEquals(userOutDto, out);
    }
}