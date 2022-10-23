package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.UserClient;
import ru.practicum.shareit.user.dto.UserInDto;
import ru.practicum.shareit.user.dto.UserOutDto;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    final UserClient client;

    @GetMapping
    public List<UserOutDto> findAll(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId) {
        return client.findAll(userId);
    }

    @GetMapping("/{id}")
    public UserOutDto findById(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                               @PathVariable Long id) {
        return client.findById(userId, id);
    }

    @PostMapping
    public UserOutDto save(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                           @Valid @RequestBody UserInDto userInDto) {
        return client.save(userId, userInDto);
    }

    @PatchMapping("/{id}")
    public UserOutDto update(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                             @PathVariable Long id, @RequestBody UserInDto userInDto) {
        return client.update(userId, id, userInDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                           @PathVariable Long id) {
        client.deleteById(userId, id);
    }
}
