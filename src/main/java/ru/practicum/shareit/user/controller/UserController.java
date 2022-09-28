package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserInDto;
import ru.practicum.shareit.user.dto.UserOutDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    final UserService userService;

    @GetMapping
    public List<UserOutDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserOutDto findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping
    public UserOutDto save(@Valid @RequestBody UserInDto userInDto) {
        return userService.save(userInDto);
    }

    @PatchMapping("/{id}")
    public UserOutDto update(@PathVariable Long id, @RequestBody UserInDto userInDto) {
        return userService.update(id, userInDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }
}
