package ru.yandex.practicum.filmorate.dao.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LikeDto {
    private Long filmId;
    private Long userId;
}
