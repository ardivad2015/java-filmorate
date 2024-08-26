package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.error.ValidationErrorResponse;
import ru.yandex.practicum.filmorate.validation.error.Violation;
import ru.yandex.practicum.filmorate.validation.exceptions.ValidationRequestException;
import ru.yandex.practicum.filmorate.validation.exceptions.ValidationRequestNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
public class FilmService extends BaseService<Film> {

    private final Logger logger = LoggerFactory.getLogger(FilmService.class);

    public Film save(Film film) {
        logger.debug("Starting of saving film {}", film.toString());
        film.setId(nextId());
        logger.debug("film id {}", film.getId());
        storage.put(film.getId(), film);
        logger.trace("film was saved");
        return film;
    }

    public Film update(Film film) {
        logger.debug("Starting of film updating {}", film.toString());
        onUpdateServiceValidation(film);
        storage.put(film.getId(), film);
        logger.info("Film was updated");
        return film;
    }

    public void onUpdateServiceValidation(Film film) {
        logger.info("Starting film on update check");
        final List<Violation> violations = new ArrayList<>();
        if (film.getId() == null) {
            logger.debug("Film id is NULL");
            violations.add(new Violation("id","id не может быть пустым", null));
            throw new ValidationRequestException(new ValidationErrorResponse(violations));
        } else if (storage.get(film.getId()) == null) {
            logger.debug("Film was not found by id {}", film.getId());
            violations.add(new Violation("id","Не найден фильм по id", film.getId().toString()));
            throw new ValidationRequestNotFoundException(new ValidationErrorResponse(violations));
        }
        logger.info("Film on update check was passed");
    }
}
