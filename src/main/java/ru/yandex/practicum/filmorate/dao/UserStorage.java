package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {

    Collection<User> all();

    User save(User user);

    User update(User newUser);

    void delete(User user);

    void addFriend(User user, Long friendId);

    void removeFriend(User user, Long friendId);

    User findById(Long id);

    Map<String, Long> getEmails();

    Map<String, Long> getLogins();
}
