package ru.yandex.practicum.filmorate.dao.memory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class InMemoryFilmStorage extends IdBasedInMemoryStorage<Film> implements FilmStorage {

    private final InMemoryGenreStorage inMemoryGenreStorage;
    private final InMemoryFilmRatingStorage inMemoryFilmRatingStorage;

    @Override
    public Collection<FilmDto> all() {
        return getAll().stream()
                .map(film ->
                        FilmMapper.mapToFilmDto(film, inMemoryFilmRatingStorage.getMap(), inMemoryGenreStorage.getMap())
                )
                .toList();
    }

    @Override
    public FilmDto save(FilmDto filmDto) {
        filmDto.setId(nextId());
        log.debug("set id {}", filmDto);
        Film film = FilmMapper.mapToFilm(filmDto);
        storage.put(film.getId(), film);
        return findById(film.getId());
    }

    @Override
    public FilmDto update(FilmDto filmDto) {
        Film newfilm = FilmMapper.mapToFilm(filmDto);
        Film film = storage.get(filmDto.getId());
        if (Objects.nonNull(film)) {
            newfilm.addLikes(film.getLikes());
        }
        storage.put(film.getId(), newfilm);
        return findById(film.getId());
    }

    @Override
    public void delete(FilmDto filmDto) {
    }

    @Override
    public FilmDto findById(Long filmId) {
        Film film = findInStorageById(filmId)
                .orElseThrow(() -> {
                    log.error("findById. Film by id = {} not found", filmId);
                    return new NotFoundException(String.format("Фильм с id = %d не найден", filmId));
                });
        return FilmMapper.mapToFilmDto(film,inMemoryFilmRatingStorage.getMap(), inMemoryGenreStorage.getMap());
    }

    @Override
    public void addLike(FilmDto filmDto, Long userId) {
        Film film = storage.get(filmDto.getId());
        if (Objects.nonNull(film)) {
            film.addLike(userId);
        }
    }

    @Override
    public void removeLike(FilmDto filmDto, Long userId) {
        Film film = storage.get(filmDto.getId());
        if (Objects.nonNull(film)) {
            film.removeLike(userId);
        }
    }

    @Override
    public List<FilmDto> findPopular(int count) {
        return getAll().stream()
                .sorted(Comparator.comparingInt(film -> -film.getLikes().size()))
                .limit(count)
                .map(film ->
                    FilmMapper.mapToFilmDto(film, inMemoryFilmRatingStorage.getMap(), inMemoryGenreStorage.getMap())
                )
                .toList();
    }
}
