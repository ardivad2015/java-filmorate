package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.EnumDto;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmRatingRowMapper implements RowMapper<EnumDto> {
    @Override
    public EnumDto mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        EnumDto filmRating = new EnumDto();
        filmRating.setId(resultSet.getInt("rating_id"));
        filmRating.setName(resultSet.getString("rating_name"));
        return filmRating;
    }
}