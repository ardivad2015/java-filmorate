package ru.yandex.practicum.filmorate.dao.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.dto.EnumDto;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.FilmRating;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestDatabase
@ComponentScan("ru/yandex/practicum/filmorate")
class DBStoragesTest {

    @Autowired
    private FilmDBStorage filmDBStorage;

    @Autowired
    private UserDBStorage userDBStorage;

    @Autowired
    private GenreDBStorage genreDBStorage;

    @Autowired
    private FilmRatingDBStorage filmRatingDBStorage;

    private FilmDto createFilm() {
        FilmDto filmDto = new FilmDto();
        filmDto.setName("Film1");
        filmDto.setDescription("Descr");
        filmDto.setDuration(120);
        EnumDto rating = new EnumDto();
        rating.setId(1);
        filmDto.setRating(rating);
        filmDto.setReleaseDate(LocalDate.now());
        return filmDto;
    }

    private User createUser() {
        User user = new User();
        user.setName("User1");
        user.setEmail("Email");
        user.setLogin("login");
        user.setBirthday(LocalDate.now());
        return user;
    }

    @Test
    public void sizeIncreasesWhenSaveFilm() {
        filmDBStorage.save(createFilm());
        assertEquals(1, filmDBStorage.all().size());
    }

    @Test
    public void returnUpdatedNameWhenUpdateFilm() {
        FilmDto filmDto1 = createFilm();
        filmDto1 = filmDBStorage.save(filmDto1);
        filmDto1.setName("Film2");
        filmDBStorage.update(filmDto1);
        FilmDto updatedFilm = filmDBStorage.findById(filmDto1.getId());
        assertEquals(filmDto1.getName(), updatedFilm.getName());
    }

    @Test
    public void returnPopularFilms() {
        FilmDto filmDto1 = createFilm();
        FilmDto filmDto2 = createFilm();
        FilmDto filmDto3 = createFilm();
        FilmDto filmDto4 = createFilm();

        filmDto1 = filmDBStorage.save(filmDto1);
        filmDto2 = filmDBStorage.save(filmDto2);
        filmDto3 = filmDBStorage.save(filmDto3);
        filmDto4 = filmDBStorage.save(filmDto4);

        userDBStorage.save(createUser());
        userDBStorage.save(createUser());
        userDBStorage.save(createUser());
        userDBStorage.save(createUser());
        userDBStorage.save(createUser());

        filmDBStorage.addLike(filmDto3, 1L);
        filmDBStorage.addLike(filmDto3, 2L);
        filmDBStorage.addLike(filmDto3, 3L);
        filmDBStorage.addLike(filmDto2, 1L);
        filmDBStorage.addLike(filmDto2, 2L);
        filmDBStorage.addLike(filmDto1, 3L);

        List<FilmDto> films = filmDBStorage.findPopular(3);
        assertEquals(4,filmDBStorage.all().size());
        assertEquals(3,films.size());
        assertEquals(filmDto3.getId(),films.get(0).getId());
        assertEquals(filmDto2.getId(),films.get(1).getId());
        assertEquals(filmDto1.getId(),films.get(2).getId());
    }

    @Test
    public void sizeIncreasesWhenSaveUser() {
        userDBStorage.save(createUser());
        assertEquals(1, userDBStorage.all().size());
    }

    @Test
    public void returnUpdatedNameWhenUpdateUser() {
        User user1 = createUser();
        user1 = userDBStorage.save(user1);
        user1.setName("Film2");
        userDBStorage.update(user1);
        User updatedUser = userDBStorage.findById(user1.getId());
        assertEquals(user1.getName(), updatedUser.getName());
    }

    @Test
    public void oneSideFriendship() {
        User user1 = createUser();
        User user2 = createUser();
        user1 = userDBStorage.save(user1);
        user2 = userDBStorage.save(user2);
        userDBStorage.addFriend(user1, user2.getId());
        assertEquals(1, userDBStorage.findById(user1.getId()).getFriends().size());
        assertEquals(0, userDBStorage.findById(user2.getId()).getFriends().size());
    }

    @Test
    public void sizeOfGenresIsEquals6() {
        assertEquals(6,genreDBStorage.all().size());
    }

    @Test
    public void secondGenreIsDrama() {
        assertEquals(Genre.DRAMA.getName(),genreDBStorage.findById(2).getName());
    }

    @Test
    public void sizeOfFilmRatingsIsEquals5() {
        assertEquals(5,filmRatingDBStorage.all().size());
    }

    @Test
    public void secondFilmRatingIsPG() {
        assertEquals(FilmRating.PG.getName(),filmRatingDBStorage.findById(2).getName());
    }


}