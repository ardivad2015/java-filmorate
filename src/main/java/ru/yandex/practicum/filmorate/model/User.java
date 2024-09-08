package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(of = { "id" })
public class User {

    private Long id;
    private Set<Long> friends = new HashSet<>();

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

    public void removeFriend(Long friendId) {
        friends.remove(friendId);
    }
}

