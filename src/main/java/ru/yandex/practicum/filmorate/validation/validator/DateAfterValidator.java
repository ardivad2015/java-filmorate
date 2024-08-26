package ru.yandex.practicum.filmorate.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.validation.annotation.DateAfter;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateAfterValidator implements ConstraintValidator<DateAfter, LocalDate> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private String startDate;

    @Override
    public void initialize(DateAfter constraint) {
        this.startDate = constraint.startDate();
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext context) {
        try {
            return localDate.isAfter(LocalDate.parse(startDate,DATE_TIME_FORMATTER));
        } catch (DateTimeException e) {
            return false;
        }
    }
}

