package ru.yandex.practicum.filmorate.dao.memory;

import ru.yandex.practicum.filmorate.dto.EnumDto;
import ru.yandex.practicum.filmorate.model.Named;

import java.util.*;

public class EnumBasedInMemoryStorage<T extends Named> {

    protected final Map<Integer, T> storage = new HashMap<>();

    protected Optional<EnumDto> findInStorageById(int id) {
        T element = storage.get(id);
        if (Objects.isNull(element)) {
           return Optional.empty();
        }
        EnumDto enumDto = new EnumDto();
        enumDto.setId(id);
        enumDto.setName(element.getName());
        return Optional.of(enumDto);
    }

    protected Collection<EnumDto> getAll() {
        return storage.entrySet().stream()
                .map(entry -> {
                    EnumDto enumDto = new EnumDto();
                    enumDto.setId(entry.getKey());
                    enumDto.setName(entry.getValue().getName());
                    return enumDto;
                })
                .toList();
    }

    protected Map<Integer, T> getMap() {
        return new HashMap<>(storage);
    }

}
