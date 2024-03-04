package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private Map<Integer, Film> filmsMap = new HashMap<>();
    private int filmId;

    @GetMapping
    public List<Film> listAllUsers() {
        return new ArrayList<>(filmsMap.values());
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        validateFilm(film);

        film.setId(++filmId);
        filmsMap.put(filmId, film);
        log.info("Новый фильм добавлен");
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException {
        validateFilm(film);

        if (filmsMap.remove(film.getId()) != null) {
            filmsMap.put(film.getId(), film);
            log.info("Фильм обновлён");
            return film;
        } else {
            log.error("Попытка обновления фильма с несуществующим Id");
            throw new ValidationException("Попытка обновления фильма с несуществующим Id");
        }
    }

    private void validateFilm (Film film) throws ValidationException {
        if (film.getName().isEmpty()) {
            log.error("Ошибка добавления нового фильма. Пустое название");
            throw new ValidationException("Ошибка добавления нового фильма. Пустое название");
        }

        if (film.getDescription().length() > 200) {
            log.error("Ошибка добавление нового фильма. Превышена максимальная длина описания");
            throw new ValidationException("Ошибка добавление нового фильма. Превышена максимальная длина описания");
        }

        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            log.error("Ошибка добавления нового фильма. Дата релиза");
            throw new ValidationException("Ошибка добавления нового фильма. Дата релиза");
        }

        if (film.getDuration() < 0) {
            log.error("Ошибка добавления нового фильма. Отрицательная продолжительность");
            throw new ValidationException("Ошибка добавления нового фильма. Отрицательная продолжительность");
        }
    }
}

