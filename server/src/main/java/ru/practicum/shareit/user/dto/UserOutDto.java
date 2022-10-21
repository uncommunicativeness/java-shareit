package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserOutDto {
    Long id;
    String name;
    String email;
}
