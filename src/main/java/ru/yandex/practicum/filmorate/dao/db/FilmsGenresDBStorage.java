package ru.yandex.practicum.filmorate.dao.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.dto.FilmsGenresDto;
import ru.yandex.practicum.filmorate.dto.FilmDto;

import java.util.List;

@Repository
public class FilmsGenresDBStorage extends BaseDBStorage<FilmsGenresDto> {

    private static final String FIND_ALL_QUERY = "SELECT *, g.genre_name FROM films_genres f LEFT JOIN genres g " +
            "ON f.genre_id = g.genre_id";
    private static final String FIND_BY_FILM_ID_QUERY = "SELECT *,g.genre_name FROM films_genres f LEFT JOIN genres g" +
            " ON f.genre_id = g.genre_id WHERE film_id = ?";
    private static final String SIMPLE_INSERT_QUERY = "INSERT INTO films_genres(film_id, genre_id) VALUES (?, ?)";
    private static final String DELETE_BY_FILM_ID_QUERY = "DELETE FROM films_genres WHERE film_id = ?";
    private static final String DELETE_ONE_QUERY = "DELETE FROM films_genres WHERE film_id = ? AND genre_id = ?";

    public FilmsGenresDBStorage(JdbcTemplate jdbc, RowMapper<FilmsGenresDto> mapper) {
        super(jdbc, mapper);
    }

    public List<FilmsGenresDto> all() {
        return findManyInStorage(FIND_ALL_QUERY);
    }

    public List<FilmsGenresDto> findByFilmId(Long filmId) {
        return findManyInStorage(FIND_BY_FILM_ID_QUERY, filmId);
    }

    private void save(Long filmId, int genreId) {
        simpleInsertInStorage(
                SIMPLE_INSERT_QUERY,
                filmId,
                genreId
        );
    }

    public void saveRows(FilmDto film) {
        film.getGenres().stream()
                .distinct()
                .forEach(genre -> save(film.getId(), genre.getId()));
    }

    public void deleteOne(FilmsGenresDto filmsGenresDto) {
        deleteInStorage(
                DELETE_ONE_QUERY,
                filmsGenresDto.getFilmId(),
                filmsGenresDto.getGenre().getId()
        );
    }

    private void deleteByFilmId(Long filmId) {
        deleteInStorage(DELETE_BY_FILM_ID_QUERY, filmId);
    }

    public void updateByFilm(FilmDto film) {
        deleteByFilmId(film.getId());
        saveRows(film);
    }

}
