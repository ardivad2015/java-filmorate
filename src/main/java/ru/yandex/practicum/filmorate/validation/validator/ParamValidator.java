package ru.yandex.practicum.filmorate.validation.validator;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;

@Slf4j
public class ParamValidator {

    public static void idValidation(Long id,String paramName) {
        if (id == null || id < 0) {
            log.error("idValidation. id {}, param name {}", id, paramName);
            throw ConditionsNotMetException
                    .simpleConditionsNotMetException(String.format("Параметр %s должен быть положительным", paramName));
        }
    }
}
