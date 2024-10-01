package ru.yandex.practicum.filmorate.dao.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreStorage;
import ru.yandex.practicum.filmorate.dto.EnumDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

@Component
@Slf4j
public class InMemoryGenreStorage extends EnumBasedInMemoryStorage<Genre> implements GenreStorage {

    public InMemoryGenreStorage() {
        storage.put(1, Genre.COMEDY);
        storage.put(2, Genre.DRAMA);
        storage.put(3, Genre.CARTOON);
        storage.put(4, Genre.THRILLER);
        storage.put(5, Genre.DOCUMENTARY);
        storage.put(6, Genre.ACTION);
    }

    @Override
    public Collection<EnumDto> all() {
        return getAll();
    }

    @Override
    public EnumDto findById(int genreId) {
        return findInStorageById(genreId).orElseThrow(() -> {
            log.error("findById. Genre by id = {} not found", genreId);
            return new NotFoundException(String.format("Жанр с id = %d не найден", genreId));
        });
    }
}
