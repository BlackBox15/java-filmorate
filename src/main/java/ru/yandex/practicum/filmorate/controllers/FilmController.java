package ru.yandex.practicum.filmorate.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NoSuchObjectException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findMostRated() {
        return filmService.findAll();
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException {
        return filmService.update(film);
    }

    @DeleteMapping
    public Film remove(@RequestBody Film film) throws NullPointerException {
        return filmService.remove(film);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseBody
    public Film likeFilm(@PathVariable int id, @PathVariable int userId) {
        return filmService.likeFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseBody
    public Film deleteLike(@PathVariable int id, @PathVariable int userId) {
        return filmService.deleteLike(id, userId);
    }

    @GetMapping(value = { "/popular?count={count}", "/popular"})
    @ResponseBody
    public List<Film> findTopTen(@RequestParam(required = false) int count) {
        if (count == 0) return filmService.findTopRated();
        else return filmService.findTopRated(count);
    }
}

