package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {

    Collection<User> all();

    void save(User user);

    void update(User newUser, User user);

    void delete(User user);

    User findById(Long id);

    Map<String, Long> getEmails();

    Map<String, Long> getLogins();
}
