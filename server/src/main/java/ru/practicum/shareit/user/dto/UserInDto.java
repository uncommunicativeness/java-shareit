package ru.practicum.shareit.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInDto {
    String name;
    String email;
}
