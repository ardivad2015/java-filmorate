package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.validation.validator.ParamValidator;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    public Collection<Film> all() {
        return filmStorage.all();
    }

    public Film save(Film film) {
        log.debug("starting saving {}", film);
        filmStorage.save(film);
        return film;
    }

    public Film update(Film film) {
        log.debug("starting updating {}", film);
        onUpdateCheck(film);
        filmStorage.update(film);
        return film;
    }

    public Film findById(Long filmId) {
        final Film film = filmStorage.findById(filmId);
        return Optional.ofNullable(film)
                .orElseThrow(() -> {
                    log.error("film by id = {} not found", filmId);
                    return new NotFoundException(String.format("Фильм с id = %d не найден", filmId));
                });
    }

    public Film likeIt(Long filmId, User user) {
        log.debug("like it filmId = {}, user = {}", filmId, user);
        final Film film = findById(filmId);
        film.getLikes().add(user.getId());
        return film;
    }

    public Film unLikeIt(Long filmId, User user) {
        log.debug("like it filmId = {}, user = {}", filmId, user);
        final Film film = findById(filmId);
        film.getLikes().remove(user.getId());
        return film;
    }

    public Collection<Film> findPopular(int count) {
        log.debug("find top {} popular", count);
        return all().stream()
                .sorted(Comparator.comparingInt(film -> -film.getLikes().size()))
                .limit(count)
                .toList();
    }

    private void onUpdateCheck(Film film) {
        final Long filmId = film.getId();
        ParamValidator.idValidation(filmId, "film.id");
        findById(filmId);
    }
}
