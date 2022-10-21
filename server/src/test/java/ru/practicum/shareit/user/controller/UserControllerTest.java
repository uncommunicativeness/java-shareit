package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserInDto;
import ru.practicum.shareit.user.dto.UserOutDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(UserController.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserControllerTest {
    private final UserInDto userInDto = UserInDto.builder()
            .name("Name")
            .email("name@email")
            .build();
    private final UserInDto updateUserInDto = UserInDto.builder()
            .name("Update name")
            .build();
    private final UserOutDto userOutDto = UserOutDto.builder()
            .id(1L)
            .name("Name")
            .email("name@email")
            .build();
    private final UserOutDto updateUserOutDto = UserOutDto.builder()
            .id(1L)
            .name("Update name")
            .email("name@email")
            .build();
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void findEmpty() {
        when(userService.findAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(userService, times(1)).findAll();
    }

    @Test
    @SneakyThrows
    void save() {
        when(userService.save(userInDto)).thenReturn(userOutDto);

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userInDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(userInDto.getName())))
                .andExpect(jsonPath("$.email", is(userInDto.getEmail())));
    }

    @Test
    @SneakyThrows
    void findAll() {
        when(userService.findAll()).thenReturn(List.of(userOutDto));
        mockMvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name", is(userInDto.getName())))
                .andExpect(jsonPath("$.[0].email", is(userInDto.getEmail())));
    }

    @Test
    @SneakyThrows
    void findById() {
        when(userService.findById(1L)).thenReturn(userOutDto);
        mockMvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(userInDto.getName())))
                .andExpect(jsonPath("$.email", is(userInDto.getEmail())));
    }

    @Test
    @SneakyThrows
    void updateUser() {
        when(userService.update(anyLong(), any())).thenReturn(updateUserOutDto);
        mockMvc.perform(patch("/users/{id}", 1L)
                        .content(objectMapper.writeValueAsString(updateUserInDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(updateUserInDto.getName())));
    }

    @Test
    @SneakyThrows
    void deleteUser() {
        userService.deleteById(anyLong());
        Mockito.verify(userService, times(1)).deleteById(anyLong());

        mockMvc.perform(delete("/users/{id}", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}