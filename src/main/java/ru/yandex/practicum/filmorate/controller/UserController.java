package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<User> findAll() {
        log.info("Call all method user service");
        return userService.all();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Call save method user service");
        return userService.save(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Call update method user service");
        return userService.update(user);
    }
}
