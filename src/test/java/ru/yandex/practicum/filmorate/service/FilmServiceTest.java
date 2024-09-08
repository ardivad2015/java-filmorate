package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceTest {

    private final FilmService filmService = new FilmService(new InMemoryFilmStorage());

    @Test
    public void returnFilmWhenTheCheckPassedBeforeSave() {
        final Film film1 = new Film();
        assertSame(filmService.save(film1), film1); //проверяем ссылку

        final Film film2 = new Film();
        assertSame(filmService.save(film2), film2); //проверяем ссылку
        assertEquals(filmService.all().size(),2);
    }

    @Test
    public void returnUserWhenTheCheckPassedBeforeUpdate() {
        final Film film1 = new Film();
        assertSame(filmService.save(film1), film1);

        final Film film2 = new Film();
        film2.setId(film1.getId());

        assertSame(filmService.update(film2), film2);
    }

    @Test
    public void throwExceptionsWhenTheCheckCrashesBeforeUpdate() {
        final Film film1 = new Film();
        assertSame(filmService.save(film1), film1);

        final Film film2 = new Film();
        assertThrows(ConditionsNotMetException.class,
                () -> filmService.update(film2));

        film2.setId(2L);
        assertThrows(NotFoundException.class,
                () -> filmService.update(film2));
    }

    @Test
    public void findById() {
        final Film film1 = new Film();
        filmService.save(film1);
        final Film film2 = filmService.findById(film1.getId());
        assertSame(film1, film2);
    }

    @Test
    public void findByIdUnknown() {
        final Film film1 = new Film();
        filmService.save(film1);
        assertThrows(NotFoundException.class,
                () -> filmService.findById(film1.getId() + 1));
    }

    @Test
    public void sizeLikesIncreasesWhenLikeIt() {
        final Film film1 = new Film();
        filmService.save(film1);
        final User user = new User();
        user.setId(1L);
        filmService.likeIt(film1.getId(), user);
        assertEquals(film1.getLikes().size(),1);
        assertTrue(film1.getLikes().contains(user.getId()));
    }

    @Test
    public void find3Popular() {
        for (int i = 0; i < 10; i++) {
            Film film = new Film();
            for (int j = 0; j < i; j++) {
                film.getLikes().add((long) j);
            }
            filmService.save(film);
        }

        Collection<Film> films = filmService.findPopular(3);
        assertEquals(films.size(),3);
        assertEquals(films.iterator().next().getLikes().size(),9);
    }
}