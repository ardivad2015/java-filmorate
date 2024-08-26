package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public Collection<User> findAll() {
        logger.info("Call all method user service");
        return userService.all();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        logger.info("Call save method user service");
        return userService.save(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        logger.info("Call update method user service");
        return userService.update(user);
    }
}
