package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.memory.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private final UserService userService = new UserService(new InMemoryUserStorage());

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

        assertEquals(userService.all().size(), 2);
    }

    @Test
    public void userNameByLoginWhenItsEmpty() {
        final User user1 = new User();
        user1.setEmail("aa@aaa.aa");
        user1.setLogin("aa");
        user1.setId(1L);
        assertSame(userService.save(user1), user1); //проверяем ссылку
        assertEquals(user1.getLogin(), user1.getName());
    }

    @Test
    public void throwExceptionsWhenTheCheckCrashesBeforeSave() {
        final User user1 = new User();
        user1.setEmail("aa@aaa.aa");
        user1.setLogin("aa");
        userService.save(user1);

        final User user2 = new User(); //same email
        user2.setEmail("aa@aaa.aa");

        assertThrows(ConditionsNotMetException.class,
                () -> userService.save(user2));

        user2.setEmail("bbb@aaa.aa");
        user2.setLogin("aa");//same login

        assertThrows(ConditionsNotMetException.class,
                () -> userService.save(user2));

        assertEquals(userService.all().size(), 1);
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
        assertThrows(ConditionsNotMetException.class,
                () -> userService.update(user3));

        user3.setId(3L);
        assertThrows(NotFoundException.class,
                () -> userService.update(user3));

        user3.setEmail(user2.getEmail());
        user3.setId(user1.getId());
        assertThrows(ConditionsNotMetException.class,
                () -> userService.update(user3));

        user3.setEmail("cc@ccc.cc");
        user3.setLogin(user2.getLogin());
        assertThrows(ConditionsNotMetException.class,
                () -> userService.update(user3));

    }

    @Test
    public void sizeFriendsIncreasesWhenAddFriend() {
        final User user1 = new User();
        user1.setEmail("aa@aaa.aa");
        user1.setLogin("aa");
        userService.save(user1); //проверяем ссылку

        final User user2 = new User();
        user2.setEmail("bb@aaa.aa");
        user2.setLogin("bb");
        userService.save(user2);
        userService.addFriend(user1.getId(), user2.getId());
        assertEquals(user1.getFriends().size(), 1);
        assertEquals(user1.getFriends().iterator().next(), user2.getId());
    }

    @Test
    public void getAllFriends() {
        final User user1 = new User();
        user1.setEmail("aa@aaa.aa");
        user1.setLogin("aa");
        userService.save(user1); //проверяем ссылку

        final User user2 = new User();
        user2.setEmail("bb@aaa.aa");
        user2.setLogin("bb");
        userService.save(user2);

        final User user3 = new User();
        user3.setEmail("bb@aaa.ff");
        user3.setLogin("cc");
        userService.save(user3);
        userService.addFriend(user1.getId(), user2.getId());
        userService.addFriend(user1.getId(), user3.getId());
        Collection<User> allFriends = userService.allFriends(user1.getId());
        assertEquals(allFriends.size(), 2);
        assertTrue(allFriends.containsAll(new ArrayList<>(Arrays.asList(user2, user3))));
    }

    @Test
    public void commonFriends() {
        final User user1 = new User();
        user1.setEmail("aa@aaa.aa");
        user1.setLogin("aa");
        userService.save(user1); //проверяем ссылку

        final User user2 = new User();
        user2.setEmail("bb@aaa.aa");
        user2.setLogin("bb");
        userService.save(user2);

        final User user3 = new User();
        user3.setEmail("bb@aaa.ff");
        user3.setLogin("cc");
        userService.save(user3);
        userService.addFriend(user1.getId(), user2.getId());
        userService.addFriend(user3.getId(), user2.getId());
        Collection<User> commonFriends = userService.commonFriends(user1.getId(), user3.getId());
        assertEquals(commonFriends.size(), 1);
        assertTrue(commonFriends.contains(user2));
    }

    @Test
    public void findById() {
        final User user = new User();
        userService.save(user);
        final User user2 = userService.findById(user.getId());
        assertSame(user, user2);
    }

    @Test
    public void findByIdUnknown() {
        final User user = new User();
        userService.save(user);
        assertThrows(NotFoundException.class,
                () -> userService.findById(user.getId() + 1));
    }
}