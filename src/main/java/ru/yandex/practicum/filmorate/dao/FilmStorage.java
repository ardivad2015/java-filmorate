package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.dto.FilmDto;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {

    Collection<FilmDto> all();

    FilmDto save(FilmDto film);

    FilmDto update(FilmDto film);

    void delete(FilmDto film);

    void addLike(FilmDto film, Long userId);

    void removeLike(FilmDto film, Long userId);

    FilmDto findById(Long id);

    List<FilmDto> findPopular(int count);
}

