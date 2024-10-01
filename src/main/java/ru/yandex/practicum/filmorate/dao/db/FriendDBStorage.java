package ru.yandex.practicum.filmorate.dao.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.dto.FriendDto;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Repository
public class FriendDBStorage extends BaseDBStorage<FriendDto> {

    private static final String FIND_ALL_QUERY = "SELECT * FROM friends";
    private static final String FIND_BY_USER_ID_QUERY = "SELECT * FROM friends WHERE user_id = ?";
    private static final String SIMPLE_INSERT_QUERY = "INSERT INTO friends(user_id, friend_id) VALUES (?, ?)";
    private static final String DELETE_BY_USER_ID_QUERY = "DELETE FROM friends WHERE user_id = ?";
    private static final String DELETE_ONE_QUERY = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";

    public FriendDBStorage(JdbcTemplate jdbc, RowMapper<FriendDto> mapper) {
        super(jdbc, mapper);
    }

    protected List<FriendDto> all() {
        return findManyInStorage(FIND_ALL_QUERY);
    }

    protected List<FriendDto> findByUserId(Long userId) {
        return findManyInStorage(FIND_BY_USER_ID_QUERY, userId);
    }

    protected void save(Long userId, Long friendId) {
        simpleInsertInStorage(
                SIMPLE_INSERT_QUERY,
                userId,
                friendId
        );
    }

    protected void saveRows(User user) {
        user.getFriends()
                .forEach(friendId -> save(user.getId(), friendId));
    }

    protected void deleteOne(FriendDto friendDto) {
        deleteInStorage(
                DELETE_ONE_QUERY,
                friendDto.getUserId(),
                friendDto.getFriendId()
        );
    }

    protected void deleteByUserId(Long userId) {
        deleteInStorage(DELETE_BY_USER_ID_QUERY, userId);
    }

    protected void updateByUser(User user) {
        deleteByUserId(user.getId());
        saveRows(user);
    }

}
