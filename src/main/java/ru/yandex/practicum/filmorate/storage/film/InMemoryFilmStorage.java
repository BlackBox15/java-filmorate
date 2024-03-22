package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NoSuchObjectException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> filmsMap = new HashMap<>();
    private int filmId;

    @Override
    public Film create(Film film) {
        validateFilm(film);
        film.setId(++filmId);
        filmsMap.put(filmId, film);
        log.info("Новый фильм добавлен");
        return film;
    }

    @Override
    public Film remove(Film film) {
        return filmsMap.remove(film.getId());
    }

    @Override
    public Film update(Film film) {
        validateFilm(film);

        if (filmsMap.remove(film.getId()) != null) {
            filmsMap.put(film.getId(), film);
            log.info("Фильм обновлён");
            return film;
        }
        throw new NoSuchObjectException("не найден фильм для обновления");
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(filmsMap.values());
    }

    @Override
    public Film likeFilm(int filmId, int userId) {
        if (filmsMap.containsKey(filmId)) {
            filmsMap.get(filmId).getLikes().add(userId);
            log.info("Рейтинг фильма увеличен");
            return filmsMap.get(filmId);
        }
        throw new NoSuchObjectException("Ошибка при добавлении рейтинга");
    }

    @Override
    public Film deleteLike(int filmId, int userId) {
        if (filmsMap.containsKey(filmId)) {
            filmsMap.get(filmId).getLikes().remove(userId);
            return filmsMap.get(filmId);
        }
        throw new NoSuchObjectException("Ошибка удаления рейтинга");
    }

    @Override
    public List<Film> findTopRated(int count) {
        List<Film> sortedFilms = filmsMap.values().stream().sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size()).collect(Collectors.toList());

        if (filmsMap.size() >= count) {
            return sortedFilms.subList(0, count);
        } else {
            return sortedFilms;
        }
    }

    @Override
    public List<Film> findTopRated() {
        if (filmsMap.size() >= 10) {
            return findTopRated(10);
        }
        throw new NoSuchObjectException("недостаточно фильмов в коллекции");
    }

    private void validateFilm(Film film) throws ValidationException {
        if (film.getName().isEmpty()) {
            log.error("Ошибка добавления нового фильма. Пустое название");
            throw new ValidationException("Ошибка добавления нового фильма. Пустое название");
        }

        if (film.getDescription().length() > 200) {
            log.error("Ошибка добавления нового фильма. Превышена максимальная длина описания");
            throw new ValidationException("Ошибка добавления нового фильма. Превышена максимальная длина описания");
        }

        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            log.error("Ошибка добавления нового фильма. Дата релиза");
            throw new ValidationException("Ошибка добавления нового фильма. Дата релиза");
        }

        if (film.getDuration() < 0) {
            log.error("Ошибка добавления нового фильма. Отрицательная продолжительность");
            throw new ValidationException("Ошибка добавления нового фильма. Отрицательная продолжительность");
        }
    }
}
