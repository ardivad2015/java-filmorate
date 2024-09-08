package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validation.annotation.DateAfter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(of = { "id" })
public class Film {

    private Long id;
    private Set<Long> likes = new HashSet<>();

    @NotBlank
    private String name;

    @NotBlank
    @Size(max = 200)
    private String description;

    @DateAfter(startDate = "1895-12-28")
    private LocalDate releaseDate;

    @Positive
    private int duration;

}
