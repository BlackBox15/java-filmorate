package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    List<Film> films = new ArrayList<>();
    private int filmId;

    @GetMapping
    public List<Film> listAllUsers() {
        return films;
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        if (!validateUser(film)) {
            log.debug("Ошибка создания нового фильма.");
            throw new ValidationException("Аргумент film не прошёл проверку");
        }
        log.info("Новый фильм добавлен.");
        film.setId(++filmId);
        films.add(film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException {
        if (!validateUser(film)) {
            log.debug("Ошибка при обновлении фильма.");
            throw new ValidationException("Аргумент film не прошёл проверку");
        }
        return films.stream()
                .filter(item -> item.getId() == film.getId())
                .findFirst().map(item -> {
                    log.info("Успешное обновление фильма.");
                    films.remove(item);
                    films.add(film);
                    return film;
                })
                .orElseThrow();
    }

    private boolean validateUser(Film film) {
        // название не может быть пустым
        if (film.getName().isEmpty()) return false;

        // максимальная длина описания — 200 символов
        if (film.getDescription().length() > 200) return false;

        // дата релиза — не раньше 28 декабря 1895 года
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) return false;

        // продолжительность фильма должна быть положительной
        if (film.getDuration() < 0) return false;

        return true;
    }
}

