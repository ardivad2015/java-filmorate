package ru.yandex.practicum.filmorate.storage.base;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class IdBasedInMemoryStorage<T> {

    protected final Map<Long, T> storage = new HashMap<>();
    protected Long id = 0L;

    protected Long nextId() {
        return ++id;
    }

    public T findById(Long id) {
        return storage.get(id);
    }

    public Collection<T> all() {
        return storage.values();
    }
}
