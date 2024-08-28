package ru.yandex.practicum.filmorate.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BaseService<T> {

    protected final Map<Long, T> storage = new HashMap<>();
    protected Long id = 0L;

    protected Long nextId() {
        return ++id;
    }

    public Collection<T> all() {
        return storage.values();
    }
}
