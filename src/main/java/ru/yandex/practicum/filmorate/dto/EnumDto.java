package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = { "id" })
public class EnumDto {
        @Positive
        private int id;
        private String name;
}
