package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.*;

@Data
@EqualsAndHashCode(of = { "id" })
public class User {

    private Long id;
    private final Set<Long> friends = new HashSet<>();

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "\\S*")
    private String login;

    private String name;

    @NotNull
    @PastOrPresent
    private LocalDate birthday;

    public void addFriend(Long friendId) {
        friends.add(friendId);
    }

    public void addFriends(Set<Long> friendsId) {
        friends.addAll(friendsId);
    }

    public void removeFriend(Long friendId) {
        friends.remove(friendId);
    }


}

