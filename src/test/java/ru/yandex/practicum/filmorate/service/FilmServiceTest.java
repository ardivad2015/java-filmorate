package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.dao.memory.InMemoryFilmRatingStorage;
import ru.yandex.practicum.filmorate.dao.memory.InMemoryGenreStorage;
import ru.yandex.practicum.filmorate.dao.memory.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.dto.EnumDto;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.dao.memory.InMemoryFilmStorage;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceTest {

    private final UserStorage userStorage = new InMemoryUserStorage();
    private final InMemoryGenreStorage genreStorage = new InMemoryGenreStorage();
    private final InMemoryFilmRatingStorage filmRatingStorage = new InMemoryFilmRatingStorage();
    private final InMemoryFilmStorage filmStorage = new InMemoryFilmStorage(genreStorage, filmRatingStorage);
    private final UserService userService = new UserService(userStorage);
    private final GenreService genreService = new GenreService(genreStorage);
    private final FilmRatingService filmRatingService = new FilmRatingService(filmRatingStorage);
    private final FilmService filmService = new FilmService(filmStorage, userService, genreService, filmRatingService);

    @Test
    public void sizeFriendsIncreasesWhenSave() {
        final FilmDto film1 = new FilmDto();
        EnumDto rating = new EnumDto();
        rating.setId(1);
        film1.setRating(rating);

        filmService.save(film1);
        assertEquals(filmService.all().size(),1);
    }

    @Test
    public void returnUpdatedNameWhenUpdateFilm() {
        final FilmDto film1 = new FilmDto();
        film1.setName("Film1");
        EnumDto rating = new EnumDto();
        rating.setId(1);
        film1.setRating(rating);
        assertEquals(filmService.save(film1), film1);

        final FilmDto film2 = new FilmDto();
        film2.setName("Film2");
        film2.setId(film1.getId());
        film2.setRating(rating);

        assertEquals(filmService.update(film2).getName(), film2.getName());
    }

    @Test
    public void throwExceptionsWhenTheCheckCrashesBeforeUpdate() {
        final FilmDto film1 = new FilmDto();
        EnumDto rating = new EnumDto();
        rating.setId(1);
        film1.setRating(rating);
        filmService.save(film1);

        final FilmDto film2 = new FilmDto();
        assertThrows(ConditionsNotMetException.class,
                () -> filmService.update(film2));

        film2.setId(2L);
        assertThrows(NotFoundException.class,
                () -> filmService.update(film2));
    }

    @Test
    public void findById() {
        final FilmDto film1 = new FilmDto();
        EnumDto rating = new EnumDto();
        rating.setId(1);
        film1.setRating(rating);
        filmService.save(film1);
        final FilmDto film2 = filmService.findById(film1.getId());
        assertEquals(film1, film2);
    }

    @Test
    public void findByIdUnknown() {
        final FilmDto film1 = new FilmDto();
        EnumDto rating = new EnumDto();
        rating.setId(1);
        film1.setRating(rating);
        filmService.save(film1);
        assertThrows(NotFoundException.class,
                () -> filmService.findById(film1.getId() + 1));
    }
}