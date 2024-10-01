package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.dto.FriendDto;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FriendDtoRowMapper implements RowMapper<FriendDto> {
    @Override
    public FriendDto mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return FriendDto.builder()
                .userId(resultSet.getLong("user_id"))
                .friendId(resultSet.getLong("friend_id"))
                .build();
    }
}