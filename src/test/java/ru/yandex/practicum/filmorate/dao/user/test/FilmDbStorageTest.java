package ru.yandex.practicum.filmorate.dao.user.test;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.dao.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private final GenreDbStorage genreDbStorage;
    private final MpaDbStorage mpaDbStorage;

    @Test
    public void testAllFilmIds() {
        Film film1 = new Film();
        film1.setName("Ну погоди");
        film1.setDuration(50);
        film1.setReleaseDate(LocalDate.parse("1980-01-01"));

        Film film2 = new Film();
        film2.setName("Ну погоди 2");
        film2.setDuration(50);
        film2.setReleaseDate(LocalDate.parse("1984-01-01"));
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate, genreDbStorage, mpaDbStorage);

        Film checkedFilm1 = filmDbStorage.create(film1);
        film1.setId(checkedFilm1.getId());

        Film checkedFilm2 = filmDbStorage.create(film2);
        film2.setId(checkedFilm2.getId());

        List<Integer> actualIds = List.of(film1.getId(), film2.getId());
        List<Integer> checkedIds = filmDbStorage.allFilmId();
        assertThat(checkedIds).isEqualTo(actualIds);
    }
}
