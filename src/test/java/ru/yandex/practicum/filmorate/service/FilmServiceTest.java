package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.exceptions.ValidationRequestException;
import ru.yandex.practicum.filmorate.validation.exceptions.ValidationRequestNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceTest {

    private final FilmService filmService = new FilmService();

    @Test
    public void returnFilmWhenTheCheckPassedBeforeSave() {
        final Film film1 = new Film();
        assertSame(filmService.save(film1), film1); //проверяем ссылку

        final Film film2 = new Film();
        assertSame(filmService.save(film2), film2); //проверяем ссылку
        assertEquals(filmService.storage.size(),2);
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
        assertThrows(ValidationRequestException.class,
                () -> filmService.update(film2));

        film2.setId(2L);
        assertThrows(ValidationRequestNotFoundException.class,
                () -> filmService.update(film2));
    }
}