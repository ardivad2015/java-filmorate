package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;
    private final Logger logger = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public Collection<Film> findAll() {
        logger.info("Call all method film service");
        return filmService.all();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        logger.info("Call save method film service");
        return filmService.save(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        logger.info("Call update method film service");
        return filmService.update(film);
    }

}
