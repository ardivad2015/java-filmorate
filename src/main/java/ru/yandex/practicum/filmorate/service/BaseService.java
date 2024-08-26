package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class BaseService<T> {

    protected final Map<Long, T> storage = new HashMap<>();

    public Long nextId() {
        long currentMaxId = storage.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public Collection<T> all() {
        return storage.values();
    }
}
