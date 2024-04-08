package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findMostRated() {
        log.debug("Получен запрос GET на получение списка фильмов");
        return filmService.findAll();
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        log.debug("Получен запрос POST на создание фильма \"{}\"", film.getName());
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException {
        log.debug("Получен запрос PUT на обновление фильма \"{}\"", film.getName());
        return filmService.update(film);
    }

    @DeleteMapping
    public Film remove(@RequestBody Film film) throws NullPointerException {
        log.debug("Получен запрос DELETE на удаление фильма \"{}\"", film.getName());
        return filmService.remove(film);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseBody
    public Film likeFilm(@PathVariable int id, @PathVariable int userId) {
        log.debug("Получен запрос PUT на повышение рейтинга фильма с id {} пользователем с id {} ", id, userId);
        return filmService.likeFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseBody
    public Film deleteLike(@PathVariable int id, @PathVariable int userId) {
        log.debug("Получен запрос DELETE на удаление рейтинга фильма с id {} пользователем с id {} ", id, userId);
        return filmService.deleteLike(id, userId);
    }

    @GetMapping(value = { "/popular?count={count}", "/popular"})
    @ResponseBody
    public List<Film> findTopTen(@RequestParam(required = false) int count) {
        log.debug("Получен запрос GET на получение списка рейтинговых фильмов");
        if (count <= 0) return filmService.findTopRated();
        else return filmService.findTopRated(count);
    }

    @GetMapping(value = { "/{id}"})
    @ResponseBody
    public Film findWithGenre(@RequestParam(required = true) int id) {
        log.debug("Получен запрос GET");
        return filmService.findWithGenre(id);
    }
}

