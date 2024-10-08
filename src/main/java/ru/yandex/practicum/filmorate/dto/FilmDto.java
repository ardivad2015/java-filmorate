package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validation.annotation.DateAfter;

import java.time.LocalDate;
import java.util.*;

@Data
@EqualsAndHashCode(of = {"id"})
public class FilmDto {

    private Long id;
    private List<EnumDto> genres = new ArrayList<>();
    @JsonProperty("mpa")
    @NotNull
    private EnumDto rating;

    @NotBlank
    private String name;

    @NotBlank
    @Size(max = 200)
    private String description;

    @DateAfter(startDate = "1895-12-28")
    private LocalDate releaseDate;

    @Positive
    private int duration;

    public void addGenres(List<EnumDto> newGenres) {
        genres.addAll(newGenres);
    }

    public int getRatingId() {
        return this.rating.getId();
    }

}

