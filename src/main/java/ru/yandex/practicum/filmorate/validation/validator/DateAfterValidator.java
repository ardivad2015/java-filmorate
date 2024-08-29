package ru.yandex.practicum.filmorate.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.validation.annotation.DateAfter;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DateAfterValidator implements ConstraintValidator<DateAfter, LocalDate> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private LocalDate startDate;

    @Override
    public void initialize(DateAfter constraint) {
           this.startDate = LocalDate.parse(constraint.startDate(),DATE_TIME_FORMATTER);
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext context) {
        if (Objects.isNull(localDate)) {
            return false;
        }
        try {
            return localDate.isAfter(startDate);
        } catch (DateTimeException e) {
            return false;
        }
    }
}

