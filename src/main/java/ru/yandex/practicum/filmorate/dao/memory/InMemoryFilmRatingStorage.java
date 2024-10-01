package ru.yandex.practicum.filmorate.dao.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmRatingStorage;
import ru.yandex.practicum.filmorate.dto.EnumDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.FilmRating;

import java.util.Collection;

@Component
@Slf4j
public class InMemoryFilmRatingStorage extends EnumBasedInMemoryStorage<FilmRating> implements FilmRatingStorage {

    public InMemoryFilmRatingStorage() {
       /*
    G (1, "G"),
    PG (2, "PG"),
    PG13 (3, "PG-13"),
    R (4, "R"),
    NC17 (5, "NC-17");*/
        storage.put(1, FilmRating.G);
        storage.put(2, FilmRating.PG);
        storage.put(3, FilmRating.PG13);
        storage.put(4, FilmRating.R);
        storage.put(5, FilmRating.NC17);
    }

    @Override
    public Collection<EnumDto> all() {
        return getAll();
    }

    @Override
    public EnumDto findById(int ratingId) {
        return findInStorageById(ratingId)
                .orElseThrow(() -> {
                    log.error("findById. Rating by id = {} not found", ratingId);
                    return new NotFoundException(String.format("Рейтинг с id = %d не найден", ratingId));
                });
    }
}
