package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@Slf4j
public class GenreController {
    private final GenreService genreService;

    public GenreController(final GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    @ResponseBody
    public List<Genre> findAll() {
        log.debug("GET на получение списка всех жанров");
        return genreService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Genre findById(@PathVariable int id) {
        log.debug("GET на получение жанра с id {}", id);
        return genreService.findById(id);
    }
}
