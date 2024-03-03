package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    List<Film> films = new ArrayList<>();
    private int filmId;

    // получение всех фильмов
    @GetMapping
    public List<Film> listAllUsers() {
        return films;
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        if (!validateUser(film)) {
            throw new ValidationException("Аргумент film не прошёл проверку");
        }
        film.setId(++filmId);
        films.add(film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException {
        if (!validateUser(film)) {
            throw new ValidationException("Аргумент film не прошёл проверку");
        }
        if (films.remove(film)) {
            films.add(film);
        }
        return film;
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

