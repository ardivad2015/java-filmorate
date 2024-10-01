package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.EnumDto;
import ru.yandex.practicum.filmorate.model.Named;

public class EnumMapper {

    public static EnumDto mapToEnumDto(int genreId, Named named) {
        EnumDto enumDto = new EnumDto();
        enumDto.setId(genreId);
        enumDto.setName(named.getName());
        return enumDto;
    }
}
