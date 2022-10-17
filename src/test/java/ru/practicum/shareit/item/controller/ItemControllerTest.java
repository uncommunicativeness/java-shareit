package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.comment.CommentOutDto;
import ru.practicum.shareit.item.dto.item.ItemOutDto;
import ru.practicum.shareit.item.dto.item.ItemWithBookingDateOutDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(ItemController.class)
class ItemControllerTest {
    private final ItemOutDto itemOutDto = ItemOutDto.builder()
            .id(1L)
            .name("Name")
            .description("Description")
            .available(true)
            .comments(Set.of(ItemOutDto.Comment.builder()
                    .id(1L)
                    .text("Text")
                    .authorName("Name")
                    .created(LocalDateTime.MIN)
                    .build()))
            .requestId(1L)
            .build();

    private final ItemOutDto itemOutDtoUpdate = ItemOutDto.builder()
            .id(1L)
            .name("Update")
            .description("Description")
            .available(true)
            .comments(Set.of(ItemOutDto.Comment.builder()
                    .id(1L)
                    .text("Text")
                    .authorName("Name")
                    .created(LocalDateTime.MIN)
                    .build()))
            .requestId(1L)
            .build();

    private final ItemWithBookingDateOutDto itemWithBookingDateOutDto = ItemWithBookingDateOutDto.builder()
            .id(1L)
            .name("Name")
            .description("Description")
            .available(true)
            .lastBooking(ItemWithBookingDateOutDto.Booking.builder()
                    .id(1L)
                    .bookerId(2L)
                    .build())
            .nextBooking(ItemWithBookingDateOutDto.Booking.builder()
                    .id(2L)
                    .bookerId(3L)
                    .build())
            .comments(Set.of(ItemWithBookingDateOutDto.Comment.builder()
                    .id(1L)
                    .text("Text")
                    .authorName("Name")
                    .created(LocalDateTime.MIN)
                    .build()))
            .build();
    private final CommentOutDto commentOutDto = CommentOutDto.builder()
            .id(1L)
            .text("Text")
            .authorName("Name")
            .build();
    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void findById() {
        when(itemService.findById(anyLong(), anyLong()))
                .thenReturn(itemWithBookingDateOutDto);

        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(itemWithBookingDateOutDto.getName())))
                .andExpect(jsonPath("$.description", is(itemWithBookingDateOutDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemWithBookingDateOutDto.getAvailable())))
                .andExpect(jsonPath("$.lastBooking.id", is(1)))
                .andExpect(jsonPath("$.lastBooking.bookerId", is(2)))
                .andExpect(jsonPath("$.nextBooking.id", is(2)))
                .andExpect(jsonPath("$.comments[0].id", is(1)));
    }

    @Test
    @SneakyThrows
    void findAllByOwnerId() {
        when(itemService.findAllByOwnerId(anyLong(), anyInt(), anyInt())).thenReturn(List.of(itemWithBookingDateOutDto));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].name", is(itemWithBookingDateOutDto.getName())))
                .andExpect(jsonPath("$.[0].description", is(itemWithBookingDateOutDto.getDescription())))
                .andExpect(jsonPath("$.[0].available", is(itemWithBookingDateOutDto.getAvailable())))
                .andExpect(jsonPath("$.[0].lastBooking.id", is(1)))
                .andExpect(jsonPath("$.[0].lastBooking.bookerId", is(2)))
                .andExpect(jsonPath("$.[0].nextBooking.id", is(2)))
                .andExpect(jsonPath("$.[0].comments[0].id", is(1)));

    }

    @Test
    @SneakyThrows
    void search() {
        when(itemService.search(anyString(), anyInt(), anyInt())).thenReturn(List.of(itemOutDto));

        mockMvc.perform(get("/items/search")
                        .param("text", "text")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].name", is(itemOutDto.getName())))
                .andExpect(jsonPath("$.[0].description", is(itemOutDto.getDescription())))
                .andExpect(jsonPath("$.[0].available", is(itemOutDto.getAvailable())))
                .andExpect(jsonPath("$.[0].comments[0].id", is(1)));
    }

    @Test
    @SneakyThrows
    void saveItem() {
        when(itemService.saveItem(anyLong(), any())).thenReturn(itemOutDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(itemOutDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(itemOutDto.getName())))
                .andExpect(jsonPath("$.description", is(itemOutDto.getDescription())));
    }

    @Test
    @SneakyThrows
    void saveComment() {
        when(itemService.saveComment(anyLong(), anyLong(), any()))
                .thenReturn(commentOutDto);

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .param("text", "text")
                        .content(objectMapper.writeValueAsString(commentOutDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.text", is(commentOutDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentOutDto.getAuthorName())));
    }

    @Test
    @SneakyThrows
    void update() {
        when(itemService.update(anyLong(), anyLong(), any()))
                .thenReturn(itemOutDtoUpdate);

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(itemOutDtoUpdate))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(itemOutDtoUpdate.getName())))
                .andExpect(jsonPath("$.description", is(itemOutDtoUpdate.getDescription())));
    }
}
