package ru.yandex.practicum.filmorate.dao.film;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

public interface FilmStorage {
    Film create(Film film);

    void remove(Film film);

    Film update(Film film);

    List<Film> findAll();

    Film likeFilm(int filmId, int userId);

    Film findWithGenre(int filmId);

    Film deleteLike(int filmId, int userId);

    List<Film> findTopRated(int count);

    List<Film> findTopRated();
}
