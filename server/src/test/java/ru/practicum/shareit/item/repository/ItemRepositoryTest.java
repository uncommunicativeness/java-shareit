package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.item.ItemInDto;
import ru.practicum.shareit.item.dto.item.ItemOutDto;
import ru.practicum.shareit.item.dto.item.ItemWithBookingDateOutDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRepositoryTest {
    private final ItemInDto itemInDto = ItemInDto.builder()
            .name("Name")
            .description("Description")
            .available(true)
            .build();
    private final ItemOutDto itemOutDto = ItemOutDto.builder()
            .id(1L)
            .name("Name")
            .description("Description")
            .available(true)
            .comments(Set.of(ItemOutDto.Comment.builder()
                    .id(1L)
                    .text("text")
                    .authorName("Name")
                    .created(LocalDateTime.now())
                    .build()))
            .build();
    private final ItemWithBookingDateOutDto itemWithBookingDateOutDto = ItemWithBookingDateOutDto.builder()
            .id(1L)
            .name("Name")
            .description("Description")
            .available(true)
            .nextBooking(ItemWithBookingDateOutDto.Booking.builder()
                    .id(1L)
                    .bookerId(2L)
                    .build())
            .lastBooking(ItemWithBookingDateOutDto.Booking.builder()
                    .id(2L)
                    .bookerId(3L)
                    .build())
            .comments(Set.of(ItemWithBookingDateOutDto.Comment.builder()
                    .id(1L)
                    .text("text")
                    .authorName("Name")
                    .created(LocalDateTime.now())
                    .build()))
            .build();
    User user;
    Item item;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        user = userRepository.save(User.builder()
                .id(1L)
                .name("Name")
                .email("name@email")
                .build());
        item = itemRepository.save(Item.builder()
                .id(1L)
                .name("Name")
                .description("Description")
                .available(true)
                .owner(user)
                .bookings(Set.of(Booking.builder()
                        .id(1L)
                        .end(LocalDateTime.MIN)
                        .booker(user)
                        .build()))
                .comments(Set.of())
                .build());
    }

    @AfterEach
    void afterEach() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findAllByOwnerId() {
        List<Item> list = itemRepository.findAllByOwnerId(item.getOwner().getId(), Pageable.ofSize(1));
        assertEquals(list.get(0).getId(), item.getId());
        assertEquals(list.get(0).getName(), item.getName());
        assertEquals(list.get(0).getDescription(), item.getDescription());
        assertEquals(list.get(0).getAvailable(), item.getAvailable());
    }

    @Test
    void search() {
        List<Item> list = itemRepository.search("Name", Pageable.ofSize(1));
        assertEquals(list.get(0).getId(), item.getId());
        assertEquals(list.get(0).getName(), item.getName());
        assertEquals(list.get(0).getDescription(), item.getDescription());
        assertEquals(list.get(0).getAvailable(), item.getAvailable());
    }
}