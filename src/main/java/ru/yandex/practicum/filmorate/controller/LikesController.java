package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

@RequiredArgsConstructor
@Slf4j
@Validated
@RestController
@RequestMapping("/films/{id}/like")
public class LikesController {

    private final FilmService filmService;
    private final UserService userService;

    @PutMapping("/{userId}")
    public Film likeIt(@PathVariable("id") @Positive(message = "id должен быть положительным") Long filmId,
                          @PathVariable("userId")
                          @Positive(message = "userId должен быть положительным") Long userId) {
        User user = userService.findById(userId);
        return filmService.likeIt(filmId, user);
    }

    @DeleteMapping("/{userId}")
    public Film unLikeIt(@PathVariable("id") @Positive(message = "id должен быть положительным") Long filmId,
                          @PathVariable("userId")
                          @Positive(message = "userId должен быть положительным") Long userId) {
        User user = userService.findById(userId);
        return filmService.unLikeIt(filmId, user);
    }
}
