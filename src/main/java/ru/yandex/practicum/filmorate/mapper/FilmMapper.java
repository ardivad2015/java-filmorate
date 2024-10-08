package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.EnumDto;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmRating;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmMapper {

    public static Film mapToFilm(FilmDto filmDto) {
        Film film = new Film();
        film.setId(filmDto.getId());
        film.setName(filmDto.getName());
        film.setDescription(filmDto.getDescription());
        film.setReleaseDate(filmDto.getReleaseDate());
        film.setDuration(filmDto.getDuration());

        EnumDto filmRating = filmDto.getRating();
        if (Objects.nonNull(filmRating)) {
            film.setRating(filmRating.getId());
        }

        List<EnumDto> genresDto = filmDto.getGenres();
        if (Objects.nonNull(genresDto)) {
            film.addGenres(genresDto.stream()
                    .map(EnumDto::getId)
                    .collect(Collectors.toSet()));
        }
        return film;
    }

    public static FilmDto mapToFilmDto(Film film, Map<Integer, FilmRating> ratings, Map<Integer, Genre> genres) {
        FilmDto filmDto = new FilmDto();
        filmDto.setId(film.getId());
        filmDto.setName(film.getName());
        filmDto.setDescription(film.getDescription());
        filmDto.setReleaseDate(film.getReleaseDate());
        filmDto.setDuration(film.getDuration());
        int rating = film.getRating();
        filmDto.setRating(EnumMapper.mapToEnumDto(rating,ratings.get(rating)));

        List<Integer> filmGenresIds = film.getGenres();
        if (Objects.nonNull(filmGenresIds)) {
            filmDto.setGenres(
                    filmGenresIds.stream()
                            .map(genreId -> EnumMapper.mapToEnumDto(genreId,genres.get(genreId)))
                            .toList()
            );
        }
        return filmDto;
    }
}
