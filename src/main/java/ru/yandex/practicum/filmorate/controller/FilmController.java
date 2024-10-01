package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public Collection<FilmDto> findAll() {
        return filmService.all();
    }

    @GetMapping("/{id}")
    public FilmDto findFilm(@PathVariable("id") @Positive Long filmId) {
        return filmService.findById(filmId);
    }

    @GetMapping("/popular")
    public Collection<FilmDto> findPopular(@RequestParam(defaultValue = "10") @NotNull @Positive int count) {
        return filmService.findPopular(count);
    }

    @PostMapping
    public FilmDto create(@Valid @RequestBody FilmDto film) {
       return filmService.save(film);
    }

    @PutMapping
    public FilmDto update(@Valid @RequestBody FilmDto film) {
        return filmService.update(film);
    }
}
