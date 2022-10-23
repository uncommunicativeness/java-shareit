package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.item.ItemInDto;
import ru.practicum.shareit.item.dto.item.ItemOutDto;
import ru.practicum.shareit.item.dto.item.ItemWithBookingDateOutDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ItemMapper {
    public Item toItem(ItemInDto dto) {
        return Item.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .available(dto.getAvailable())
                .build();
    }

    public ItemOutDto toIemOutDto(Item item) {
        ItemOutDto dto = ItemOutDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequest() != null ? item.getRequest().getId() : null)
                .build();

        if (item.getComments() != null) {
            dto.setComments(item.getComments()
                    .stream()
                    .filter(Objects::nonNull)
                    .map(comment -> ItemOutDto.Comment.builder()
                            .id(comment.getId())
                            .text(comment.getText())
                            .authorName(comment.getAuthor().getName())
                            .created(comment.getCreated())
                            .build()).collect(Collectors.toSet()));
        }

        return dto;
    }

    public ItemWithBookingDateOutDto toItemWithBookingDateOutDto(Item item,
                                                                 ItemWithBookingDateOutDto.Booking lastBooking,
                                                                 ItemWithBookingDateOutDto.Booking nextBooking) {
        ItemWithBookingDateOutDto dto = ItemWithBookingDateOutDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .build();

        if (item.getComments() != null) {
            dto.setComments(item.getComments()
                    .stream()
                    .filter(Objects::nonNull)
                    .map(comment -> ItemWithBookingDateOutDto.Comment.builder()
                            .id(comment.getId())
                            .text(comment.getText())
                            .authorName(comment.getAuthor().getName())
                            .created(comment.getCreated())
                            .build()).collect(Collectors.toSet()));
        }

        return dto;
    }
}
