package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.exceptions.ValidationRequestException;
import ru.yandex.practicum.filmorate.validation.exceptions.ValidationRequestNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private final UserService userService = new UserService();

    @Test
    public void returnUserWhenTheCheckPassedBeforeSave() {
        final User user1 = new User();
        user1.setEmail("aa@aaa.aa");
        user1.setLogin("aa");
        assertSame(userService.save(user1), user1); //проверяем ссылку

        final User user2 = new User();
        user2.setEmail("bb@aaa.aa");
        user2.setLogin("bb");
        assertSame(userService.save(user2), user2);

        assertEquals(userService.storage.size(),2);
    }

    @Test
    public void userNameByLoginWhenItsEmpty() {
        final User user1 = new User();
        user1.setEmail("aa@aaa.aa");
        user1.setLogin("aa");
        user1.setId(1L);
        assertSame(userService.save(user1), user1); //проверяем ссылку
        assertEquals(user1.getLogin(),user1.getName());
    }

    @Test
    public void throwExceptionsWhenTheCheckCrashesBeforeSave() {
        final User user1 = new User();
        user1.setEmail("aa@aaa.aa");
        user1.setLogin("aa");
        userService.save(user1);

        final User user2 = new User(); //same email
        user2.setEmail("aa@aaa.aa");

        assertThrows(ValidationRequestException.class,
                () -> userService.save(user2));

        user2.setEmail("bbb@aaa.aa");
        user2.setLogin("aa");//same login

        assertThrows(ValidationRequestException.class,
                () -> userService.save(user2));

        assertEquals(userService.storage.size(),1);
    }

    @Test
    public void returnUserWhenTheCheckPassedBeforeUpdate() {
        final User user1 = new User();
        user1.setEmail("aa@aaa.aa");
        user1.setLogin("aa");
        userService.save(user1);

        final User user2 = new User();
        user2.setEmail("bb@aaa.aa");
        user2.setLogin("bb");
        user2.setId(user1.getId());

        assertSame(userService.update(user2), user2);
    }

    @Test
    public void throwExceptionsWhenTheCheckCrashesBeforeUpdate() {
        final User user1 = new User();
        user1.setEmail("aa@aaa.aa");
        user1.setLogin("aa");
        userService.save(user1);

        final User user2 = new User();
        user2.setEmail("bb@bbb.bb");
        user2.setLogin("bb");
        userService.save(user2);

        final User user3 = new User();
        assertThrows(ValidationRequestException.class,
                () -> userService.update(user3));

        user3.setId(3L);
        assertThrows(ValidationRequestNotFoundException.class,
                () -> userService.update(user3));

        user3.setEmail(user2.getEmail());
        user3.setId(user1.getId());
        assertThrows(ValidationRequestException.class,
                () -> userService.update(user3));

        user3.setEmail("cc@ccc.cc");
        user3.setLogin(user2.getLogin());
        assertThrows(ValidationRequestException.class,
                () -> userService.update(user3));

    }
}