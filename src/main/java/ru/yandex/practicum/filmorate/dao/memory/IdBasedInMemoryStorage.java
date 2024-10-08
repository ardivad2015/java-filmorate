package ru.yandex.practicum.filmorate.dao.memory;

import java.util.*;

public class IdBasedInMemoryStorage<T> {

    protected final Map<Long, T> storage = new HashMap<>();
    protected Long id = 0L;

    protected Long nextId() {
        return ++id;
    }

    protected Optional<T> findInStorageById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public Collection<T> getAll() {
        return storage.values();
    }
}
