package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(BookingController.class)
class BookingControllerTest {
    private final LocalDateTime start = LocalDateTime.now().plusHours(1);
    private final LocalDateTime end = LocalDateTime.now().plusDays(1);
    private final BookingOutDto bookingOutDto = BookingOutDto.builder()
            .id(1L)
            .start(start)
            .end(end)
            .status(BookingOutDto.Status.WAITING)
            .booker(BookingOutDto.Booker.builder().id(1L).build())
            .item(BookingOutDto.Item.builder().id(1L).build())
            .build();

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void findById() {
        when(bookingService.findById(anyLong(), anyLong())).thenReturn(bookingOutDto);

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.booker.id", is(1)))
                .andExpect(jsonPath("$.item.id", is(1)));
    }

    @Test
    @SneakyThrows
    void findAllByBookerAndState() {
        when(bookingService.findAllByBookerAndState(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingOutDto));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].booker.id", is(1)))
                .andExpect(jsonPath("$[0].item.id", is(1)));
    }

    @Test
    @SneakyThrows
    void findAllByOwnerAndState() {
        when(bookingService.findAllByOwnerAndState(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingOutDto));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].booker.id", is(1)))
                .andExpect(jsonPath("$[0].item.id", is(1)));
    }

    @Test
    @SneakyThrows
    void save() {
        when(bookingService.save(anyLong(), any())).thenReturn(bookingOutDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(bookingOutDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.booker.id", is(1)))
                .andExpect(jsonPath("$.item.id", is(1)));
    }

    @Test
    @SneakyThrows
    void setStatus() {
        when(bookingService.setStatus(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingOutDto);

        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true")
                        .content(objectMapper.writeValueAsString(bookingOutDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.booker.id", is(1)))
                .andExpect(jsonPath("$.item.id", is(1)));
    }
}