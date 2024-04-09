package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.dao.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.exceptions.NoSuchObjectException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmDbStorage filmDbStorage;
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;

    public FilmService(FilmDbStorage filmDbStorage,
                       MpaDbStorage mpaDbStorage,
                       GenreDbStorage genreDbStorage) {
        this.filmDbStorage = filmDbStorage;
        this.mpaDbStorage = mpaDbStorage;
        this.genreDbStorage = genreDbStorage;
    }

    public Film create(Film film) {
//        Integer filmId = film.getId();

//        if (filmDbStorage.findAll().stream().anyMatch((f) -> f.getName().equals(film.getName()))) {
//            throw new ValidationException("Фильм с таким названием уже существует");
//        }

        if (film.getId() != null) {
            throw new NoSuchObjectException("Объект с ID нельзя добавить через create");
        }

        if (filmDbStorage.allFilmId().contains(film.getId())) {
            throw new NoSuchObjectException("Фильм с таким ID уже существует!");
        }

//        if (filmId != null && filmDbStorage.allFilmId().contains(filmId)) {
//            throw new NoSuchObjectException("Фильм с таким ID уже существует!");
//        } else if (filmId != null && filmId < 0) {
//            throw new IllegalArgumentException("отрицательный ID");
//        }

        if (!mpaDbStorage.allMpaId().contains(film.getMpa().getId())) {
            log.error("некорректный MPA");
            throw new ValidationException("некорректный MPA");
        }

        if (film.getGenres() != null) {
            for (Genre genre: film.getGenres()) {
                if (!genreDbStorage.allGenreId().contains(genre.getId())) {
                    log.error("Отсутствует Genres");
                    throw new ValidationException("Отсутствует Genres");
                }
            }
            // удаление дубликатов id жанров
            List<Genre> noDuplicates = film.getGenres()
                    .stream()
                    .distinct()
                    .collect(Collectors.toList());
            film.setGenres(noDuplicates);
        }
        return filmDbStorage.create(film);
    }

    public Film update(Film film) {
        if (!filmDbStorage.allFilmId().contains(film.getId())) {
            log.error("Отсутствует или введён несуществующий Id");
            throw new NoSuchObjectException("Отсутствует или введён несуществующий Id");
        }

        // проверка на вхождение id жанров в допустимый перечень
        if (film.getGenres() != null) {
            for (Genre genre: film.getGenres()) {
                if (!genreDbStorage.allGenreId().contains(genre.getId())) {
                    log.error("Присутствует недопустимый id жанра");
                    throw new ValidationException("Присутствует недопустимый id жанра");
                }
            }
        }

        return filmDbStorage.update(film);
    }

    public List<Film> findAll() {
        return filmDbStorage.findAll();
    }

    public void remove(Film film) {
        filmDbStorage.remove(film);
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
        if (!filmDbStorage.allFilmId().contains(filmId)) {
            log.error("Отсутствует или введён несуществующий Id");
            throw new NoSuchObjectException("Отсутствует или введён несуществующий Id");
        }
        return filmDbStorage.findWithGenre(filmId);
    }
}
