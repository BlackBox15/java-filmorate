package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validation.ValidReleaseDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class Film {
    private Integer id;
    @NotEmpty(message = "Имя не может быть пустым")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    private String description;
    @ValidReleaseDate
    @NotNull(message = "дата выхода не может быть пустой")
    private LocalDate releaseDate;
    @Positive(message = "Длительность фильма не может быть отрицательной")
    private int duration;
    private Mpa mpa;
    private List<Genre> genres;
}
