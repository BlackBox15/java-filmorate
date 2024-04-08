package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@Slf4j
public class MpaController {
    private final MpaService mpaService;

    public MpaController(final MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    @ResponseBody
    public List<Mpa> getMpa() {
        log.debug("GET на получение списка всех рейтингов");
        return mpaService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Mpa findUser(@PathVariable int id) {
        log.debug("GET на получение рейтинга с id {}", id);
        return mpaService.findById(id);
    }
}
