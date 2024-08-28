package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.error.ValidationErrorResponse;
import ru.yandex.practicum.filmorate.validation.error.Violation;
import ru.yandex.practicum.filmorate.validation.exceptions.ValidationRequestException;
import ru.yandex.practicum.filmorate.validation.exceptions.ValidationRequestNotFoundException;

import java.util.*;

@Slf4j
@Service
public class UserService extends BaseService<User> {

    private final Map<String, Long> emails = new HashMap<>();
    private final Map<String, Long> logins = new HashMap<>();

    public User save(User user) {
        log.debug("Starting of saving user {}", user.toString());
        onCreateServiceValidation(user);
        setUserNameByLogin(user);
        user.setId(nextId());
        log.debug("user id {}", user.getId());
        storage.put(user.getId(), user);
        emails.put(user.getEmail(), user.getId());
        logins.put(user.getLogin(), user.getId());
        log.info("user was saved");
        return user;
    }

    public User update(User user) {
        log.debug("Starting of updating user {}", user.toString());
        User currentUser = storage.get(user.getId());
        onUpdateServiceValidation(user, currentUser);
        emails.remove(currentUser.getEmail());
        logins.remove(currentUser.getLogin());
        storage.put(user.getId(), user);
        emails.put(user.getEmail(), user.getId());
        logins.put(user.getLogin(), user.getId());
        log.info("User was updated");
        return user;
    }

    public void onUpdateServiceValidation(User user, User currentUser) {
        log.info("Starting user on update check");
        final List<Violation> violations = new ArrayList<>();
        if (user.getId() == null) {
            log.debug("User id is NULL");
            violations.add(new Violation("id","id не может быть пустым", null));
        } else if (Objects.isNull(currentUser)) {
            log.debug("User was not found by id {}", user.getId());
            violations.add(new Violation("id","Не найден пользователь по id", user.getId().toString()));
            throw new ValidationRequestNotFoundException(new ValidationErrorResponse(violations));
        } else {
            if (emails.containsKey(user.getEmail()) && !Objects.equals(user.getId(), emails.get(user.getEmail()))) {
                log.debug("User email {} is already registered.", user.getEmail());
                violations.add(new Violation("email", "Такой email уже зарегистрирован", user.getEmail()));
            }
            if (logins.containsKey(user.getLogin()) && !Objects.equals(user.getId(), logins.get(user.getLogin()))) {
                log.debug("User login {} is already registered.", user.getLogin());
                violations.add(new Violation("login", "Такой логин уже зарегистрирован", user.getLogin()));
            }
        }
        if (!violations.isEmpty()) {
            throw new ValidationRequestException(new ValidationErrorResponse(violations));
        }
        log.info("User on update check was passed");
    }

    public void onCreateServiceValidation(User user) {
        log.info("Starting user on create check");
        final List<Violation> violations = new ArrayList<>();
        if (emails.containsKey(user.getEmail())) {
            log.debug("User email {} is already registered.", user.getEmail());
            violations.add(new Violation("email","такой email уже зарегистрирован", user.getEmail()));
        }
        if (logins.containsKey(user.getLogin())) {
            log.debug("User login {} is already registered.", user.getLogin());
            violations.add(new Violation("login","Пользователеь с таким логином уже зарегистрирован", user.getLogin()));
        }
        if (!violations.isEmpty()) {
            throw new ValidationRequestException(new ValidationErrorResponse(violations));
        }
        log.info("User on create check was passed");
    }

    private void setUserNameByLogin(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
