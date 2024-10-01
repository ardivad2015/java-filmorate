package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.service.FilmService;

@RequiredArgsConstructor
@Slf4j
@Validated
@RestController
@RequestMapping("/films/{id}/like")
public class LikesController {

    private final FilmService filmService;

    @PutMapping("/{userId}")
    public FilmDto likeIt(@PathVariable("id") @Positive(message = "id должен быть положительным") Long filmId,
                          @PathVariable("userId")
                          @Positive(message = "userId должен быть положительным") Long userId) {
        return filmService.likeIt(filmId, userId);
    }

    @DeleteMapping("/{userId}")
    public FilmDto unLikeIt(@PathVariable("id") @Positive(message = "id должен быть положительным") Long filmId,
                          @PathVariable("userId")
                          @Positive(message = "userId должен быть положительным") Long userId) {
        return filmService.unLikeIt(filmId, userId);
    }
}
