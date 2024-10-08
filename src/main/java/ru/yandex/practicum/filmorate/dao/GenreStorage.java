package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.dto.EnumDto;

import java.util.Collection;

public interface GenreStorage {

    Collection<EnumDto> all();

    EnumDto findById(int id);
}
