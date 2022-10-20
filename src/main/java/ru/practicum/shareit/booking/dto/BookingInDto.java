package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.validators.DateConstraint;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@DateConstraint
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookingInDto {
    Long itemId;

    @NotNull
    LocalDateTime start;

    @NotNull
    LocalDateTime end;
}
