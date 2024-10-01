package ru.yandex.practicum.filmorate.dao.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.FilmDto;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class FilmRowMapper implements RowMapper<FilmDto> {

    private final FilmRatingRowMapper filmRatingRowMapper;

    @Override
    public FilmDto mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        FilmDto film = new FilmDto();
        film.setId(resultSet.getLong("film_id"));
        film.setName(resultSet.getString("film_name"));
        film.setDescription(resultSet.getString("description"));
        film.setDuration(resultSet.getInt("duration"));
        film.setReleaseDate(resultSet.getTimestamp("release_date").toLocalDateTime().toLocalDate());
        film.setRating(filmRatingRowMapper.mapRow(resultSet, rowNum));
        return film;
    }
}