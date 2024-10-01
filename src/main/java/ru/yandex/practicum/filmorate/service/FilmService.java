package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.EnumDto;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundBadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.validation.validator.ParamValidator;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;
    private final GenreService genreService;
    private final FilmRatingService filmRatingService;

    public Collection<FilmDto> all() {
        return filmStorage.all();

    }

    public FilmDto save(FilmDto filmDto) {
        log.debug("starting saving {}", filmDto);
        onSaveCheck(filmDto);
        return filmStorage.save(filmDto);
    }

    public FilmDto update(FilmDto filmDto) {
        log.debug("starting updating {}", filmDto);
        onUpdateCheck(filmDto);
        return filmStorage.update(filmDto);
    }

    public FilmDto likeIt(Long filmId, Long userId) {
        log.debug("like it filmId = {}, userId = {}", filmId, userId);
        final FilmDto filmDto = findById(filmId);
        userService.findById(userId);
        filmStorage.addLike(filmDto, userId);
        return findById(filmId);
    }

    public FilmDto unLikeIt(Long filmId, Long userId) {
        log.debug("like it filmId = {}, userId = {}", filmId, userId);
        final FilmDto filmDto = filmStorage.findById(filmId);
        userService.findById(userId);
        filmStorage.removeLike(filmDto, userId);
        return findById(filmId);
    }

    public List<FilmDto> findPopular(int count) {
        log.debug("find top {} popular", count);
        return filmStorage.findPopular(count);
    }

    public FilmDto findById(Long filmId) {
        return filmStorage.findById(filmId);
    }

    private void onUpdateCheck(FilmDto film) {
        final Long filmId = film.getId();
        ParamValidator.idValidation(filmId, "film.id");
        findById(filmId);
        onSaveCheck(film);
    }

    private void onSaveCheck(FilmDto film) {
        List<String> errors = new ArrayList<>();
        film.getGenres().forEach(
                genreDto -> {
                    try {
                        genreService.findById(genreDto.getId());
                    } catch (NotFoundException e) {
                        errors.add(e.getMessage());
                    }
                }
        );
        EnumDto filmRatingDto = film.getRating();
        if (Objects.nonNull(filmRatingDto)) {
            try {
                filmRatingService.findById(filmRatingDto.getId());
            } catch (NotFoundException e) {
                errors.add(e.getMessage());
            }
        }
        if (!errors.isEmpty()) {
            StringBuilder errorBuilder = new StringBuilder();
            errors.forEach(error -> {
                        errorBuilder.append(error);
                        errorBuilder.append("\n");
                    }
            );
            throw new NotFoundBadRequestException(errorBuilder.toString()); //только из-за postman тестов
        }
    }

}