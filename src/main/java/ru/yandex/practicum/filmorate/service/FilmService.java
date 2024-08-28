package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.error.ValidationErrorResponse;
import ru.yandex.practicum.filmorate.validation.error.Violation;
import ru.yandex.practicum.filmorate.validation.exceptions.ValidationRequestException;
import ru.yandex.practicum.filmorate.validation.exceptions.ValidationRequestNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FilmService extends BaseService<Film> {

    public Film save(Film film) {
        log.debug("Starting of saving film {}", film.toString());
        film.setId(nextId());
        log.debug("film id {}", film.getId());
        storage.put(film.getId(), film);
        log.trace("film was saved");
        return film;
    }

    public Film update(Film film) {
        log.debug("Starting of film updating {}", film.toString());
        onUpdateServiceValidation(film);
        storage.put(film.getId(), film);
        log.info("Film was updated");
        return film;
    }

    public void onUpdateServiceValidation(Film film) {
        log.info("Starting film on update check");
        final List<Violation> violations = new ArrayList<>();
        if (film.getId() == null) {
            log.debug("Film id is NULL");
            violations.add(new Violation("id","id не может быть пустым", null));
            throw new ValidationRequestException(new ValidationErrorResponse(violations));
        } else if (storage.get(film.getId()) == null) {
            log.debug("Film was not found by id {}", film.getId());
            violations.add(new Violation("id","Не найден фильм по id", film.getId().toString()));
            throw new ValidationRequestNotFoundException(new ValidationErrorResponse(violations));
        }
        log.info("Film on update check was passed");
    }
}
