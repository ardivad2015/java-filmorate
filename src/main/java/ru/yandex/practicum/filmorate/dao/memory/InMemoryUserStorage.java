package ru.yandex.practicum.filmorate.dao.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage extends IdBasedInMemoryStorage<User> implements UserStorage {

    private final Map<String, Long> emails = new HashMap<>();
    private final Map<String, Long> logins = new HashMap<>();

    @Override
    public Collection<User> all() {
        return getAll();
    }

    @Override
    public User save(User user) {
        final Long userId = nextId();
        user.setId(userId);
        log.debug("set id {}", user);
        storage.put(userId, user);
        emails.put(user.getEmail(), userId);
        logins.put(user.getLogin(), userId);
        return findById(userId);
    }

    @Override
    public User update(User newUser) {
        final Long userId = newUser.getId();
        final User user = findById(userId);
        emails.remove(user.getEmail());
        logins.remove(user.getLogin());
        storage.put(userId, newUser);
        emails.put(newUser.getEmail(), userId);
        logins.put(newUser.getLogin(), userId);
        return findById(userId);
    }

    @Override
    public void delete(User user) {
    }

    @Override
    public Map<String, Long> getEmails() {
        return new HashMap<>(emails);
    }

    @Override
    public Map<String, Long> getLogins() {
        return new HashMap<>(logins);
    }

    @Override
    public User findById(Long userId) {
        return findInStorageById(userId)
                .orElseThrow(() -> {
                    log.error("findById. User by id = {} not found", userId);
                    return new NotFoundException(String.format("Пользователь с id = %d не найден", userId));
                });
    }

    @Override
    public void addFriend(User user, Long friendId) {
        user.addFriend(friendId);
    }

    @Override
    public void removeFriend(User user, Long friendId) {
        user.removeFriend(friendId);
    }
}
