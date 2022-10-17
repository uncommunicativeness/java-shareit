package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserInDto;
import ru.practicum.shareit.user.dto.UserOutDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {
    private final UserInDto userInDto = UserInDto.builder()
            .name("Name")
            .email("name@email")
            .build();
    private final UserOutDto userOutDto = UserOutDto.builder()
            .id(1L)
            .name("Name")
            .email("name@email")
            .build();
    private final User user = User.builder()
            .id(1L)
            .name("Name")
            .email("name@email")
            .build();
    @InjectMocks
    private UserMapper mapper;

    @Test
    void toUser() {
        assertEquals(user.getName(), mapper.toUser(userInDto).getName());
        assertEquals(user.getEmail(), mapper.toUser(userInDto).getEmail());
    }

    @Test
    void toOutDto() {
        assertEquals(userOutDto.getId(), mapper.toDto(user).getId());
        assertEquals(userOutDto.getName(), mapper.toDto(user).getName());
        assertEquals(userOutDto.getEmail(), mapper.toDto(user).getEmail());
    }
}