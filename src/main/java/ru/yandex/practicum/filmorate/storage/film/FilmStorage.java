package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> all();

    void save(Film film);

    void update(Film film);

    void delete(Film film);

    Film findById(Long id);
}

