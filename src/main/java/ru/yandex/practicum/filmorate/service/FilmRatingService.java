package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmRatingStorage;
import ru.yandex.practicum.filmorate.dto.EnumDto;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmRatingService {

    private final FilmRatingStorage filmRatingStorage;

    public Collection<EnumDto> all() {
        return filmRatingStorage.all();

    }

    public EnumDto findById(int ratingId) {
        return filmRatingStorage.findById(ratingId);
    }
}