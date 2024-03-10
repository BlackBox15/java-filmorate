package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    @Override
    public Film create(Film film) {
        return null;
    }

    @Override
    public Film remove(Film film) {
        return null;
    }

    @Override
    public Film update(Film film) {
        return null;
    }
}
