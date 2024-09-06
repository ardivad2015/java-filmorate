package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.base.IdBasedInMemoryStorage;

@Component
@Slf4j
public class InMemoryFilmStorage extends IdBasedInMemoryStorage<Film> implements FilmStorage {

    @Override
    public void save(Film film) {
        film.setId(nextId());
        log.debug("set id {}", film);
        storage.put(film.getId(), film);
    }

    @Override
    public void update(Film film) {
        storage.put(film.getId(), film);
    }

    @Override
    public void delete(Film film) {
    }

}
