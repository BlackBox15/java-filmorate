package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;

import java.util.List;

@Service
public class FilmService {
    private final FilmDbStorage filmDbStorage;

    public FilmService(FilmDbStorage filmDbStorage) {
        this.filmDbStorage = filmDbStorage;
    }

    public Film create(Film film) {
        return filmDbStorage.create(film);
    }

    public Film update(Film film) {
        return filmDbStorage.update(film);
    }

    public List<Film> findAll() {
        return filmDbStorage.findAll();
    }

    public Film remove(Film user) {
        return filmDbStorage.remove(user);
    }

    public Film likeFilm(int filmId, int userId) {
        return filmDbStorage.likeFilm(filmId, userId);
    }

    public Film deleteLike(int filmId, int userId) {
        return filmDbStorage.deleteLike(filmId, userId);
    }

    public List<Film> findTopRated(int count) {
        return filmDbStorage.findTopRated(count);
    }

    public List<Film> findTopRated() {
        return filmDbStorage.findTopRated();
    }

    public Film findWithGenre(int filmId) {
        return filmDbStorage.findWithGenre(filmId);
    }


}
