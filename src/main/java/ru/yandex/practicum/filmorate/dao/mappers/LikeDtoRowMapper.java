package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.dao.dto.LikeDto;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LikeDtoRowMapper implements RowMapper<LikeDto> {

    @Override
    public LikeDto mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return LikeDto.builder()
                .filmId(resultSet.getLong("film_id"))
                .userId(resultSet.getLong("user_id"))
                .build();
    }
}