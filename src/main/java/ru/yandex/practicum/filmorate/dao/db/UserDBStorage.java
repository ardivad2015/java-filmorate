package ru.yandex.practicum.filmorate.dao.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.dto.FriendDto;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@Primary
public class UserDBStorage extends BaseDBStorage<User> implements UserStorage {

    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO users(user_name, email, login, birthday)" +
            " VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET user_name = ?, email = ?, login = ?, birthday = ?" +
            " WHERE user_id = ?";

    private final FriendDBStorage friendDBStorage;

    public UserDBStorage(JdbcTemplate jdbc, RowMapper<User> mapper, FriendDBStorage friendDBStorage) {
        super(jdbc, mapper);
        this.friendDBStorage = friendDBStorage;
    }

    @Override
    public Collection<User> all() {
        List<User> userList = findManyInStorage(FIND_ALL_QUERY);
        List<FriendDto> friendDtoList = friendDBStorage.all();
        return userList.stream()
                .peek(user -> user.addFriends(friendDtoList.stream()
                        .filter(friendDto -> friendDto.getUserId().equals(user.getId()))
                        .map(FriendDto::getFriendId)
                        .collect(Collectors.toSet())))
                .toList();
    }

    @Override
    public User save(User user) {
        long id = insertInStorage(
                INSERT_QUERY,
                "user_id",
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                Timestamp.from(user.getBirthday().atStartOfDay(ZoneId.systemDefault()).toInstant())
        );
        user.setId(id);
        friendDBStorage.saveRows(user);
        return findById(id);
    }

    @Override
    public User update(User user) {
        updateInStorage(
                UPDATE_QUERY,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                Timestamp.from(user.getBirthday().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                user.getId()
        );
        friendDBStorage.updateByUser(user);
        return findById(user.getId());
    }

    @Override
    public void delete(User user) {

    }

    @Override
    public User findById(Long userId) {
        User user = findOneInStorage(FIND_BY_ID_QUERY, userId)
                .orElseThrow(() -> {
                    log.error("findById. User by id = {} not found", userId);
                    return new NotFoundException(String.format("Пользователь с id = %d не найден", userId));
                });
        List<FriendDto> friendDtoList = friendDBStorage.findByUserId(userId);
        user.addFriends(friendDtoList.stream()
                .map(FriendDto::getFriendId)
                .collect(Collectors.toSet()));
        return user;
    }

    @Override
    public void addFriend(User user, Long friendId) {
        friendDBStorage.save(user.getId(), friendId);
    }

    @Override
    public void removeFriend(User user, Long friendId) {
        friendDBStorage.deleteOne(new FriendDto(user.getId(), friendId));
    }

    @Override
    public Map<String, Long> getEmails() {
        List<User> userList = findManyInStorage(FIND_ALL_QUERY);
        return userList.stream().collect(Collectors.toMap(User::getEmail, User::getId));
    }

    @Override
    public Map<String, Long> getLogins() {
        List<User> userList = findManyInStorage(FIND_ALL_QUERY);
        return userList.stream().collect(Collectors.toMap(User::getLogin, User::getId));
    }
}

