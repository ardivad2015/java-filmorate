package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.EnumDto;
import ru.yandex.practicum.filmorate.service.FilmRatingService;

import java.util.Collection;

@RequiredArgsConstructor
@Slf4j
@Validated
@RestController
@RequestMapping("/mpa")
public class FilmRatingController {

    private final FilmRatingService filmRatingService;

    @GetMapping
    public Collection<EnumDto> findAll() {
        return filmRatingService.all();
    }

    @GetMapping("/{id}")
    public EnumDto findGenre(@PathVariable("id") @Positive Integer ratingId) {
        return filmRatingService.findById(ratingId);
    }
}
