package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.base.IdBasedInMemoryStorage;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage extends IdBasedInMemoryStorage<User> implements UserStorage {

    private final Map<String, Long> emails = new HashMap<>();
    private final Map<String, Long> logins = new HashMap<>();

    @Override
    public void save(User user) {
        final Long userId = nextId();
        user.setId(userId);
        log.debug("set id {}", user);
        storage.put(userId, user);
        emails.put(user.getEmail(), userId);
        logins.put(user.getLogin(), userId);
    }

    @Override
    public void update(User newUser, User user) {
        final Long userId = newUser.getId();
        emails.remove(user.getEmail());
        logins.remove(user.getLogin());
        storage.put(userId, newUser);
        emails.put(newUser.getEmail(), userId);
        logins.put(newUser.getLogin(), userId);
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
}
