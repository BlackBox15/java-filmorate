package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film create(Film film);
    Film remove(Film film);
    Film update(Film film);
    List<Film> findAll();
    Film likeFilm(Long filmId, Long userId);
    Film deleteLike(Long filmId, Long userId);
    List<Film> findTopRated(int count);
    List<Film> findTopRated();

}
