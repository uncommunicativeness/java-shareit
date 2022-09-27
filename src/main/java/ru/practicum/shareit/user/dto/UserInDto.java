package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserInDto {
    @NotBlank
    String name;

    @Email
    @NotBlank
    String email;
}
