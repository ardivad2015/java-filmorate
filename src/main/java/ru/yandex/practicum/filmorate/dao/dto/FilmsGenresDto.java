package ru.yandex.practicum.filmorate.dao.dto;

import lombok.Data;
import ru.yandex.practicum.filmorate.dto.EnumDto;

@Data
public class FilmsGenresDto {
    private Long filmId;
    private EnumDto genre;
}
