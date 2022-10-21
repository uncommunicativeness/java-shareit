package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserInDto;
import ru.practicum.shareit.user.dto.UserOutDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    final UserService userService;

    @GetMapping
    public List<?> findAll(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId) {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserOutDto findById(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                               @PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping
    public UserOutDto save(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                           @RequestBody UserInDto userInDto) {
        return userService.save(userInDto);
    }

    @PatchMapping("/{id}")
    public UserOutDto update(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                             @PathVariable Long id, @RequestBody UserInDto userInDto) {
        return userService.update(id, userInDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                           @PathVariable Long id) {
        userService.deleteById(id);
    }
}
