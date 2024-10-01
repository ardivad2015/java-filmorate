package ru.yandex.practicum.filmorate.dao.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreStorage;
import ru.yandex.practicum.filmorate.dto.EnumDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;

@Slf4j
@Repository
@Primary
public class GenreDBStorage extends BaseDBStorage<EnumDto> implements GenreStorage {

    private static final String FIND_ALL_QUERY = "SELECT * FROM genres ORDER BY genre_id";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genres WHERE genre_id = ? ORDER BY genre_id";

    @Autowired
    public GenreDBStorage(JdbcTemplate jdbc, @Qualifier("genreRowMapper") RowMapper<EnumDto> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<EnumDto> all() {
        return findManyInStorage(FIND_ALL_QUERY);
    }

    @Override
    public EnumDto findById(int genreId) {
        return findOneInStorage(FIND_BY_ID_QUERY, genreId)
                .orElseThrow(() -> {
                    log.error("findById. Genre by id = {} not found", genreId);
                    return new NotFoundException(String.format("Жанр с id = %d не найден", genreId));
                });
    }

}
