package ru.practicum.shareit.user.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class UserOutDtoTest {
    @Autowired
    private JacksonTester<UserOutDto> json;

    @Test
    @SneakyThrows
    void testUserInDto() {
        UserOutDto userOutDto = UserOutDto.builder()
                .id(1L)
                .name("Name")
                .email("name@email")
                .build();

        JsonContent<UserOutDto> result = json.write(userOutDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Name");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("name@email");
    }
}