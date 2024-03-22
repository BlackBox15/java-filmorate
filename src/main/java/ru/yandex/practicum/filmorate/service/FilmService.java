package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(FilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public Film create(Film film) {
        return inMemoryFilmStorage.create(film);
    }

    public Film update(Film film) {
        return inMemoryFilmStorage.update(film);
    }

    public List<Film> findAll() {
        return inMemoryFilmStorage.findAll();
    }

    public Film remove(Film user) {
        return inMemoryFilmStorage.remove(user);
    }

    public Film likeFilm(int filmId, int userId) {
        return inMemoryFilmStorage.likeFilm(filmId, userId);
    }

    public Film deleteLike(int filmId, int userId) {
        return inMemoryFilmStorage.deleteLike(filmId, userId);
    }

    public List<Film> findTopRated(int count) {
        return inMemoryFilmStorage.findTopRated(count);
    }

    public List<Film> findTopRated() {
        return inMemoryFilmStorage.findTopRated();
    }
}
