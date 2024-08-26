package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(FilmController.class)
class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FilmService filmService;

    @Test
    void returnFilmsListWhenGet() throws Exception {
        final Film film1 = new Film();
        film1.setId(1L);

        final Film film2 = new Film();
        film1.setId(2L);

        final List<Film> films = Arrays.asList(film1, film2);
        when(filmService.all()).thenReturn(films);
        String response = this.mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(films), response);
    }

    @Test
    void postAndPutResponseStatusBadRequestWhenValidationFails() throws Exception {
        //name is NULL
        final Film film1 = new Film();
        film1.setDescription("Description");
        film1.setDuration(100);
        film1.setReleaseDate(LocalDate.now());

        perfomPostObjectBadRequest(film1);
        perfomPutObjectBadRequest(film1);

        //invalid release date
        film1.setName("film1");
        film1.setDescription("Description");
        film1.setDuration(100);
        film1.setReleaseDate(LocalDate.of(1703,1,1));

        perfomPostObjectBadRequest(film1);
        perfomPutObjectBadRequest(film1);

        //neg duration
        film1.setName("film1");
        film1.setDescription("Description");
        film1.setDuration(-100);
        film1.setReleaseDate(LocalDate.of(2003,1,1));

        perfomPostObjectBadRequest(film1);
        perfomPutObjectBadRequest(film1);

        //oversize description
        film1.setName("film1");
        film1.setDescription(Stream.generate(() -> String.valueOf("a"))
                .limit(201)
                .collect(Collectors.joining()));
        film1.setDuration(100);
        film1.setReleaseDate(LocalDate.of(2003,1,1));

        perfomPostObjectBadRequest(film1);
        perfomPutObjectBadRequest(film1);
    }

    private void perfomPostObjectBadRequest(Film film) throws Exception {
        this.mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(this.objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
        verify(filmService, never()).save(film);
    }

    private void perfomPutObjectBadRequest(Film film) throws Exception {
        this.mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(this.objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
        verify(filmService, never()).update(film);
    }
}