package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class ItemWithBookingDateDto {
    Long id;

    @NotBlank
    String name;

    @NotBlank
    String description;

    @NotNull
    Boolean available;

    @JsonInclude(JsonInclude.Include.ALWAYS)
    BookingDto lastBooking;

    @JsonInclude(JsonInclude.Include.ALWAYS)
    BookingDto nextBooking;

    Set<CommentDto> comments;
}
