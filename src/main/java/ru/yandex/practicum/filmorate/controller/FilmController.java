package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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


}

