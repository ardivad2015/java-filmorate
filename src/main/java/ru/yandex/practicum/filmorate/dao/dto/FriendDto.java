package ru.yandex.practicum.filmorate.dao.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FriendDto {
    private Long userId;
    private Long friendId;
}
