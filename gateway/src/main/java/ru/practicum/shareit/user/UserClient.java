package ru.practicum.shareit.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserInDto;
import ru.practicum.shareit.user.dto.UserOutDto;

import javax.validation.Valid;
import java.util.List;

@FeignClient(value = "users",
        path = "users",
        url = "${shareit-server.url}")
public interface UserClient {
    @GetMapping
    List<UserOutDto> findAll(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId);

    @GetMapping("/{id}")
    UserOutDto findById(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                        @PathVariable Long id);

    @PostMapping
    UserOutDto save(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                    @Valid @RequestBody UserInDto userInDto);

    @PatchMapping("/{id}")
    UserOutDto update(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                      @PathVariable Long id, @RequestBody UserInDto userInDto);

    @DeleteMapping("/{id}")
    void deleteById(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                    @PathVariable Long id);
}
