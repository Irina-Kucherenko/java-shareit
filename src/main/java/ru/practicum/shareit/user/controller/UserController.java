package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<UserDto> getAllUsers() {
        log.info("Get all users");
        return userService.getAllUsers();
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Create new user");
        return userService.createUser(userDto);
    }

    @PatchMapping(value = "/{userId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public UserDto updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        log.info("Update user");
        return userService.updateUser(userId, userDto);
    }

    @GetMapping(value = "/{userId}", produces = APPLICATION_JSON_VALUE)
    public UserDto getUserById(@PathVariable Long userId) {
        log.info("Get user by id");
        return userService.getUserById(userId);
    }

    @DeleteMapping(value = "/{userId}", produces = APPLICATION_JSON_VALUE)
    public void deleteUser(@PathVariable Long userId) {
        log.info("Delete user by id");
        userService.deleteUser(userId);
    }
}
