package ru.yandex.practicum.filmorate.util.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class ErrorResponse {
    private final List<String> errorList;
}
