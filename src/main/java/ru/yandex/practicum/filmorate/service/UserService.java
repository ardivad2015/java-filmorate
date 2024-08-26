package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.error.ValidationErrorResponse;
import ru.yandex.practicum.filmorate.validation.error.Violation;
import ru.yandex.practicum.filmorate.validation.exceptions.ValidationRequestException;
import ru.yandex.practicum.filmorate.validation.exceptions.ValidationRequestNotFoundException;

import java.util.*;

@Service
public class UserService extends BaseService<User> {

    private final Map<String, Long> emails = new HashMap<>();
    private final Map<String, Long> logins = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public User save(User user) {
        logger.debug("Starting of saving user {}", user.toString());
        onCreateServiceValidation(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(nextId());
        logger.debug("user id {}", user.getId());
        storage.put(user.getId(), user);
        emails.put(user.getEmail(), user.getId());
        logins.put(user.getLogin(), user.getId());
        logger.info("user was saved");
        return user;
    }

    public User update(User user) {
        logger.debug("Starting of updating user {}", user.toString());
        onUpdateServiceValidation(user);
        emails.remove(storage.get(user.getId()).getEmail());
        logins.remove(storage.get(user.getId()).getLogin());
        storage.put(user.getId(), user);
        emails.put(user.getEmail(), user.getId());
        logins.put(user.getLogin(), user.getId());
        logger.info("User was updated");
        return user;
    }

    public void onUpdateServiceValidation(User user) {
        logger.info("Starting user on update check");
        final List<Violation> violations = new ArrayList<>();
        if (user.getId() == null) {
            logger.debug("User id is NULL");
            violations.add(new Violation("id","id не может быть пустым", null));
        } else if (storage.get(user.getId()) == null) {
            logger.debug("User was not found by id {}", user.getId());
            violations.add(new Violation("id","Не найден пользователь по id", user.getId().toString()));
            throw new ValidationRequestNotFoundException(new ValidationErrorResponse(violations));
        } else {
            if (emails.containsKey(user.getEmail()) && !Objects.equals(user.getId(), emails.get(user.getEmail()))) {
                logger.debug("User email {} is already registered.", user.getEmail());
                violations.add(new Violation("email", "Такой email уже зарегистрирован", user.getEmail()));
            }
            if (logins.containsKey(user.getLogin()) && !Objects.equals(user.getId(), logins.get(user.getLogin()))) {
                logger.debug("User login {} is already registered.", user.getLogin());
                violations.add(new Violation("login", "Такой логин уже зарегистрирован", user.getLogin()));
            }
        }
        if (!violations.isEmpty()) {
            throw new ValidationRequestException(new ValidationErrorResponse(violations));
        }
        logger.info("User on update check was passed");
    }

    public void onCreateServiceValidation(User user) {
        logger.info("Starting user on create check");
        final List<Violation> violations = new ArrayList<>();
        if (emails.containsKey(user.getEmail())) {
            logger.debug("User email {} is already registered.", user.getEmail());
            violations.add(new Violation("email","такой email уже зарегистрирован", user.getEmail()));
        }
        if (logins.containsKey(user.getLogin())) {
            logger.debug("User login {} is already registered.", user.getLogin());
            violations.add(new Violation("login","Пользователеь с таким логином уже зарегистрирован", user.getLogin()));
        }
        if (!violations.isEmpty()) {
            throw new ValidationRequestException(new ValidationErrorResponse(violations));
        }
        logger.info("User on create check was passed");
    }
}
