package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.util.error.ErrorResponse;
import ru.yandex.practicum.filmorate.validation.validator.ParamValidator;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public Collection<User> all() {
        return userStorage.all();
    }

    public User save(User user) {
        log.debug("starting saving {}", user);
        onSaveCheck(user);
        userStorage.save(user);
        setUserNameByLogin(user);
        return user;
    }

    public User update(User newUser) {
        log.debug("starting updating {}", newUser);
        final Long newUserId = newUser.getId();
        ParamValidator.idValidation(newUserId, "user.id");
        findById(newUserId);
        onUpdateCheck(newUser);
        setUserNameByLogin(newUser);
        userStorage.update(newUser);
        return newUser;
    }

    public Collection<User> allFriends(Long userId) {
        User user = findById(userId);
        return user.getFriends().stream()
                .map(this::findById)
                .toList();
    }

    public Collection<User> commonFriends(Long userId, Long otherId) {
        User firstUser = findById(userId);
        User secondUser = findById(otherId);
        return firstUser.getFriends().stream()
                .filter(id -> secondUser.getFriends().contains(id))
                .map(this::findById)
                .toList();
    }

    public User addFriend(Long userId, Long friendId) {
        log.debug("starting adding friend userId {}, friendId {}", userId, friendId);
        final User user = findById(userId);
        findById(friendId);
        userStorage.addFriend(user, friendId);
        return user;
    }

    public User removeFriend(Long userId, Long friendId) {
        log.debug("starting removing friend userId {}, friendId {}", userId, friendId);
        final User user = findById(userId);
        findById(friendId);
        userStorage.removeFriend(user, friendId);
        return user;
    }

    public User findById(Long userId) {
        return userStorage.findById(userId);
    }

    private void onUpdateCheck(User newUser) {
        final List<String> errorList = new ArrayList<>();
        final Long userId = newUser.getId();
        final String email = newUser.getEmail();
        final String login = newUser.getLogin();
        final Map<String, Long> emails = userStorage.getEmails();
        final Map<String, Long> logins = userStorage.getLogins();
        if (emails.containsKey(email)) {
            Long emailOwner = emails.get(email);
            if (!Objects.equals(userId, emailOwner)) {
                errorList.add(String.format("Email %s уже зарегистрирован у пользователя с id %d", email, emailOwner));
                log.error("onUpdateCheck. Email {} has already been registered or the user id {}", email, emailOwner);
            }
        }
        if (logins.containsKey(login)) {
            Long loginOwner = logins.get(login);
            if (!Objects.equals(userId, loginOwner)) {
                errorList.add(String.format("Логин %s уже зарегистрирован у пользователя с id %d", login, loginOwner));
                log.error("onUpdateCheck. Login {} has already been registered or the user id {}", login, loginOwner);
            }
        }
        if (!errorList.isEmpty()) {
            throw new ConditionsNotMetException(new ErrorResponse(errorList));
        }
    }

    private void onSaveCheck(User user) {
        final List<String> errorList = new ArrayList<>();
        final String email = user.getEmail();
        final String login = user.getLogin();
        final Map<String, Long> emails = userStorage.getEmails();
        final Map<String, Long> logins = userStorage.getLogins();
        if (emails.containsKey(email)) {
            errorList.add(String.format("Email %s уже зарегистрирован", email));
            log.error("onSaveCheck. Email {} has already been registered", email);
        }
        if (logins.containsKey(login)) {
            errorList.add(String.format("Логин %s уже зарегистрирован", login));
            log.error("onSaveCheck. Login {} has already been registered", login);
        }
        if (!errorList.isEmpty()) {
            throw new ConditionsNotMetException(new ErrorResponse(errorList));
        }
    }

    private void setUserNameByLogin(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("setting name by login user {}", user);
            user.setName(user.getLogin());
        }
    }
}
