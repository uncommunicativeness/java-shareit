package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.item.ItemInDto;
import ru.practicum.shareit.item.dto.item.ItemOutDto;
import ru.practicum.shareit.item.dto.item.ItemWithBookingDateOutDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ItemMapperTest {
    private final User user = User.builder()
            .id(1L)
            .name("Name")
            .email("name@email")
            .build();
    private final Item item = Item.builder()
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
            .build();
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
    @InjectMocks
    private ItemMapper mapper;

    @Test
    void toItem() {
        assertEquals(item.getName(), mapper.toItem(itemInDto).getName());
        assertEquals(item.getDescription(), mapper.toItem(itemInDto).getDescription());
        assertEquals(item.getAvailable(), mapper.toItem(itemInDto).getAvailable());
    }

    @Test
    void toIemOutDto() {
        assertEquals(itemOutDto.getId(), mapper.toIemOutDto(item).getId());
        assertEquals(itemOutDto.getDescription(), mapper.toIemOutDto(item).getDescription());
        assertEquals(itemOutDto.getAvailable(), mapper.toIemOutDto(item).getAvailable());
    }

    @Test
    void toItemWithBookingDateOutDto() {
        assertEquals(itemWithBookingDateOutDto.getId(),
                mapper.toItemWithBookingDateOutDto(item, null, null).getId());
        assertEquals(itemWithBookingDateOutDto.getDescription(),
                mapper.toItemWithBookingDateOutDto(item, null, null).getDescription());
        assertEquals(itemWithBookingDateOutDto.getAvailable(),
                mapper.toItemWithBookingDateOutDto(item, null, null).getAvailable());
    }
}