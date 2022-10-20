package ru.practicum.shareit.user.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class UserInDtoTest {
    @Autowired
    private JacksonTester<UserInDto> json;

    @Test
    @SneakyThrows
    void testUserInDto() {
        UserInDto userDto = UserInDto.builder()
                .name("Name")
                .email("name@email")
                .build();

        JsonContent<UserInDto> result = json.write(userDto);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Name");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("name@email");
    }
}