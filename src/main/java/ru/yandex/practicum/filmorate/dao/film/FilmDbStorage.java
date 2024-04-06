package ru.yandex.practicum.filmorate.dao.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public class FilmDbStorage implements FilmStorage{
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

    @Override
    public List<Film> findAll() {
        return null;
    }

    @Override
    public Film likeFilm(int filmId, int userId) {
        return null;
    }

    @Override
    public Film deleteLike(int filmId, int userId) {
        return null;
    }

    @Override
    public List<Film> findTopRated(int count) {
        return null;
    }

    @Override
    public List<Film> findTopRated() {
        return null;
    }
}
