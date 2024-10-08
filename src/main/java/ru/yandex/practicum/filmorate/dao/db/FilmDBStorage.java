package ru.yandex.practicum.filmorate.dao.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.dao.dto.FilmsGenresDto;
import ru.yandex.practicum.filmorate.dao.dto.LikeDto;
import ru.yandex.practicum.filmorate.dto.EnumDto;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Repository
@Primary
public class FilmDBStorage extends BaseDBStorage<FilmDto> implements FilmStorage {

    private static final String FIND_ALL_QUERY = "SELECT *,r.rating_name FROM films f LEFT JOIN film_ratings r " +
            " ON f.rating_id = r.rating_id";
    private static final String FIND_BY_ID_QUERY = "SELECT *,r.rating_name FROM films f LEFT JOIN film_ratings r " +
            " ON f.rating_id = r.rating_id WHERE film_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO films(film_name, description, duration," +
            " rating_id, release_date) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE films SET film_name = ?, description = ?," +
            " duration = ?, release_date = ?, rating_id = ? WHERE film_id = ?";
    private static final String TOP_POPULAR_QUERY =
            "SELECT f.film_id, f.film_name, f.description,f.release_date, f.duration, f.rating_id, r.rating_name " +
            "FROM films f INNER JOIN " +
            "            (SELECT film_id, count(user_id) likes FROM films_likes" +
            "            GROUP BY film_id " +
            "            ORDER BY count(user_id) desc LIMIT ? ) AS top_likes " +
            "            ON f.film_id = top_likes.film_id " +
            "         LEFT JOIN film_ratings r ON f.rating_id = r.rating_id " +
            "            ORDER BY top_likes.likes desc";
    private final LikeDBStorage likeDBStorage;
    private final FilmsGenresDBStorage filmsGenresDBStorage;

    public FilmDBStorage(JdbcTemplate jdbc, RowMapper<FilmDto> mapper,
                         LikeDBStorage likeDBStorage,
                         FilmsGenresDBStorage filmsGenresDBStorage) {
        super(jdbc, mapper);
        this.likeDBStorage = likeDBStorage;
        this.filmsGenresDBStorage = filmsGenresDBStorage;
    }

    @Override
    public Collection<FilmDto> all() {
        List<FilmDto> filmList = findManyInStorage(FIND_ALL_QUERY);
        List<FilmsGenresDto> filmsGenresList = filmsGenresDBStorage.all();
        matchGenres(filmList, filmsGenresList);
        return filmList;
    }

    @Override
    public FilmDto save(FilmDto film) {
        long id = insertInStorage(
                INSERT_QUERY,
                "film_id",
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getRatingId(),
                Timestamp.from(film.getReleaseDate().atStartOfDay(ZoneId.systemDefault()).toInstant())
        );
        film.setId(id);
        filmsGenresDBStorage.saveRows(film);
        return findById(id);
    }

    @Override
    public FilmDto update(FilmDto film) {
        Long filmId = film.getId();
        updateInStorage(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                Timestamp.from(film.getReleaseDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                film.getRatingId(),
                filmId
        );
        filmsGenresDBStorage.updateByFilm(film);
        return findById(filmId);
    }

    @Override
    public void delete(FilmDto film) {
    }

    @Override
    public FilmDto findById(Long filmId) {
        FilmDto film = findOneInStorage(FIND_BY_ID_QUERY, filmId)
                .orElseThrow(() -> {
                    log.error("findById. Film by id = {} not found", filmId);
                    return new NotFoundException(String.format("Фильм с id = %d не найден", filmId));
                });
        List<FilmsGenresDto> filmsGenresList = filmsGenresDBStorage.findByFilmId(filmId);
        List<EnumDto> genres = filmsGenresList.stream()
                .map(FilmsGenresDto::getGenre)
                .toList();
        film.setGenres(genres);
        return film;
    }

    @Override
    public void addLike(FilmDto film, Long userId) {
        likeDBStorage.save(new LikeDto(film.getId(), userId));
    }

    @Override
    public void removeLike(FilmDto film, Long userId) {
        likeDBStorage.deleteOne(new LikeDto(film.getId(), userId));
    }

    @Override
    public List<FilmDto> findPopular(int count) {
        return findManyInStorage(TOP_POPULAR_QUERY, count);
    }

    private void matchGenres(List<FilmDto> filmList, List<FilmsGenresDto> filmsGenresList) {
        filmList.forEach(
                film -> film.setGenres(filmsGenresList.stream()
                        .filter(row -> row.getFilmId().equals(film.getId()))
                        .map(FilmsGenresDto::getGenre)
                        .toList())
                );
    }
}
