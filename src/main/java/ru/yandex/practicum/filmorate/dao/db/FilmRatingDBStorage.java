package ru.yandex.practicum.filmorate.dao.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmRatingStorage;
import ru.yandex.practicum.filmorate.dto.EnumDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;

@Slf4j
@Repository
@Primary
public class FilmRatingDBStorage extends BaseDBStorage<EnumDto> implements FilmRatingStorage {

    private static final String FIND_ALL_QUERY = "SELECT * FROM film_ratings";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM film_ratings WHERE rating_id = ?";

    @Autowired
    public FilmRatingDBStorage(JdbcTemplate jdbc, @Qualifier("filmRatingRowMapper") RowMapper<EnumDto> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<EnumDto> all() {
        return findManyInStorage(FIND_ALL_QUERY);
    }

    @Override
    public EnumDto findById(int ratingId) {
        return findOneInStorage(FIND_BY_ID_QUERY, ratingId)
                .orElseThrow(() -> {
                    log.error("findById. Film rating by id = {} not found", ratingId);
                    return new NotFoundException(String.format("Рейтинг с id = %d не найден", ratingId));
                });
    }

}
