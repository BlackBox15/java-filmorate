package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController filmController;

    @Test
    public void shouldBeEmptyLists() {
        assertEquals(Collections.EMPTY_LIST, filmController.listAllFilms());
    }

    @Test
    public void shouldCreateNewFilm() {
        Film testFilm = new Film();
        testFilm.setName("Star Wars 3");
        testFilm.setDescription("sci-fi");
        testFilm.setDuration(180);
        testFilm.setReleaseDate(LocalDate.parse("1972-01-01"));

        assertEquals(testFilm, filmController.create(testFilm));
    }

    @Test
    public void shouldThrowExceptionWithoutName() {
        Film testFilm = new Film();
        testFilm.setName("");
        testFilm.setDescription("sci-fi");
        testFilm.setDuration(180);
        testFilm.setReleaseDate(LocalDate.parse("1972-01-01"));

        String exceptionMessage = "Ошибка добавления нового фильма. Пустое название";

        Exception exception = Assertions.assertThrows(ValidationException.class, () ->
                filmController.create(testFilm));
        Assertions.assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWthLongDescription() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 201; i++) {
            sb.append(i);
        }

        Film testFilm = new Film();
        testFilm.setName("Star Wars 3");
        testFilm.setDescription(sb.toString());
        testFilm.setDuration(180);
        testFilm.setReleaseDate(LocalDate.parse("1972-01-01"));

        String exceptionMessage = "Ошибка добавления нового фильма. Превышена максимальная длина описания";

        Exception exception = Assertions.assertThrows(ValidationException.class, () ->
                filmController.create(testFilm));
        Assertions.assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWithWrongReleaseDate() {
        Film testFilm = new Film();
        testFilm.setName("Star Wars 3");
        testFilm.setDescription("sci-fi");
        testFilm.setDuration(180);
        testFilm.setReleaseDate(LocalDate.parse("1700-01-01"));

        String exceptionMessage = "Ошибка добавления нового фильма. Дата релиза";

        Exception exception = Assertions.assertThrows(ValidationException.class, () ->
                filmController.create(testFilm));
        Assertions.assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWithLessZeroDuration() {
        Film testFilm = new Film();
        testFilm.setName("Star Wars 3");
        testFilm.setDescription("sci-fi");
        testFilm.setDuration(-31);
        testFilm.setReleaseDate(LocalDate.parse("2023-01-01"));

        String exceptionMessage = "Ошибка добавления нового фильма. Отрицательная продолжительность";

        Exception exception = Assertions.assertThrows(ValidationException.class, () ->
                filmController.create(testFilm));
        Assertions.assertEquals(exceptionMessage, exception.getMessage());
    }
}