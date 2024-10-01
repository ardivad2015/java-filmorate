package ru.yandex.practicum.filmorate.dao.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.dto.LikeDto;

import java.util.List;

@Repository
public class LikeDBStorage extends BaseDBStorage<LikeDto> {

    private static final String FIND_ALL_QUERY = "SELECT * FROM films_likes";
    private static final String FIND_BY_FILM_ID_QUERY = "SELECT * FROM films_likes WHERE film_id = ?";
    private static final String SIMPLE_INSERT_QUERY = "INSERT INTO films_likes(film_id, user_id) VALUES (?, ?)";
    private static final String DELETE_BY_FILM_ID_QUERY = "DELETE FROM films_likes WHERE film_id = ?";
    private static final String DELETE_ONE_QUERY = "DELETE FROM films_likes WHERE user_id = ? AND film_id = ?";

    public LikeDBStorage(JdbcTemplate jdbc, RowMapper<LikeDto> mapper) {
        super(jdbc, mapper);
    }

    public List<LikeDto> all() {
        return findManyInStorage(FIND_ALL_QUERY);
    }

    public List<LikeDto> findByFilmId(Long filmId) {
        return findManyInStorage(FIND_BY_FILM_ID_QUERY, filmId);
    }

    public void save(LikeDto likeDto) {
        simpleInsertInStorage(
                SIMPLE_INSERT_QUERY,
                likeDto.getFilmId(),
                likeDto.getUserId()
        );
    }

    public void saveList(List<LikeDto> likeDtoList) {
        likeDtoList.stream()
                        .peek(this::save)
                                .toList();
    }

    public void deleteOne(LikeDto likeDtoList) {
        deleteInStorage(
                DELETE_ONE_QUERY,
                likeDtoList.getUserId(),
                likeDtoList.getFilmId()
        );
    }

    public void deleteByFilmId(Long filmId) {
        deleteInStorage(DELETE_BY_FILM_ID_QUERY, filmId);
    }

}
