package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    List<Film> films = new ArrayList<>();

    /**
     * добавление фильма;
     * обновление фильма;
     * .
     */

    // получение всех фильмов
    @GetMapping
    public List<Film> listAllUsers() {
        return films;
    }

    @PostMapping
    public void create(@RequestBody Film film) throws InvalidEmailException {
        if (user.hashCode() == 0) {
            throw new InvalidEmailException("В переданных данных отсутствует адрес электронной почты");
        }
        if(users.stream().anyMatch(item -> item.equals(user))) {
            throw new InvalidEmailException("Пользователь с указанным адресом электронной почты уже был добавлен ранее");
        }

        films.add(film);
    }

    @PutMapping
    public void update(@RequestBody Film film) throws InvalidEmailException {
        if (user.hashCode() == 0) {
            throw new InvalidEmailException("В переданных данных отсутствует адрес электронной почты");
        }

        films.remove(film);
        films.add(film);
    }
}

