package ru.yandex.practicum.filmorate.dao.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.dto.FilmsGenresDto;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class FilmsGenresDtoRowMapper implements RowMapper<FilmsGenresDto> {

    private final GenreRowMapper genreRowMapper;

    @Override
    public FilmsGenresDto mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        FilmsGenresDto filmsGenres = new FilmsGenresDto();
        filmsGenres.setFilmId(resultSet.getLong("film_id"));
        filmsGenres.setGenre(genreRowMapper.mapRow(resultSet, rowNum));
        return filmsGenres;
    }
}