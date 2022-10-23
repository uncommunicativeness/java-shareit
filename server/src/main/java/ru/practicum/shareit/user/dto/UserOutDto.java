package ru.practicum.shareit.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "id")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserOutDto {
    Long id;
    String name;
    String email;
}
