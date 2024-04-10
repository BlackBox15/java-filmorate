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
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testAllFilmIds() {
        GenreDbStorage genreDbStorage = new GenreDbStorage(jdbcTemplate);
        MpaDbStorage mpaDbStorage = new MpaDbStorage(jdbcTemplate);
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate, genreDbStorage, mpaDbStorage);

        Film film1 = new Film();
        film1.setName("Ну погоди");
        film1.setDuration(50);
        film1.setReleaseDate(LocalDate.parse("1980-01-01"));
        Mpa mpa = new Mpa();
        mpa.setId(3);
        film1.setMpa(mpa);

        Film film2 = new Film();
        film2.setName("Ну погоди 2");
        film2.setDuration(50);
        film2.setReleaseDate(LocalDate.parse("1984-01-01"));
        film2.setMpa(mpa);

        Film checkedFilm1 = filmDbStorage.create(film1);
        film1.setId(checkedFilm1.getId());

        Film checkedFilm2 = filmDbStorage.create(film2);
        film2.setId(checkedFilm2.getId());

        List<Integer> actualIds = List.of(film1.getId(), film2.getId());
        List<Integer> checkedIds = filmDbStorage.allFilmId();
        assertThat(checkedIds).isEqualTo(actualIds);
    }

    @Test
    public void testAllFilmNames() {
        GenreDbStorage genreDbStorage = new GenreDbStorage(jdbcTemplate);
        MpaDbStorage mpaDbStorage = new MpaDbStorage(jdbcTemplate);
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate, genreDbStorage, mpaDbStorage);

        Film film1 = new Film();
        film1.setName("Ну погоди");
        film1.setDuration(50);
        film1.setReleaseDate(LocalDate.parse("1980-01-01"));
        Mpa mpa = new Mpa();
        mpa.setId(3);
        film1.setMpa(mpa);

        Film film2 = new Film();
        film2.setName("Ну погоди 2");
        film2.setDuration(50);
        film2.setReleaseDate(LocalDate.parse("1984-01-01"));
        film2.setMpa(mpa);

        Film checkedFilm1 = filmDbStorage.create(film1);
        film1.setId(checkedFilm1.getId());

        Film checkedFilm2 = filmDbStorage.create(film2);
        film2.setId(checkedFilm2.getId());

        List<String> allFilmNames = filmDbStorage.allFilmNames();
        assertThat(allFilmNames.get(0)).isEqualTo(film1.getName());
        assertThat(allFilmNames.get(1)).isEqualTo(film2.getName());
    }

    @Test
    public void testCreateFilm() {
        GenreDbStorage genreDbStorage = new GenreDbStorage(jdbcTemplate);
        MpaDbStorage mpaDbStorage = new MpaDbStorage(jdbcTemplate);
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate, genreDbStorage, mpaDbStorage);

        Film film1 = new Film();
        film1.setName("Ну погоди");
        film1.setDuration(50);
        film1.setReleaseDate(LocalDate.parse("1980-01-01"));
        Mpa mpa = new Mpa();
        mpa.setId(3);
        film1.setMpa(mpa);

        Film checkedFilm1 = filmDbStorage.create(film1);
        film1.setId(checkedFilm1.getId());

        assertThat(filmDbStorage.findAll().size()).isEqualTo(1);
        assertThat(filmDbStorage.findAll().get(0).getName()).isEqualTo(film1.getName());
        assertThat(filmDbStorage.findAll().get(0).getDuration()).isEqualTo(film1.getDuration());
        assertThat(filmDbStorage.findAll().get(0).getId()).isEqualTo(film1.getId());
        assertThat(filmDbStorage.findAll().get(0).getReleaseDate()).isEqualTo(film1.getReleaseDate());
    }

    @Test
    public void testUpdateFilm() {
        GenreDbStorage genreDbStorage = new GenreDbStorage(jdbcTemplate);
        MpaDbStorage mpaDbStorage = new MpaDbStorage(jdbcTemplate);
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate, genreDbStorage, mpaDbStorage);

        Film film1 = new Film();
        film1.setName("Ну погоди");
        film1.setDuration(50);
        film1.setReleaseDate(LocalDate.parse("1980-01-01"));
        Mpa mpa = new Mpa();
        mpa.setId(3);
        film1.setMpa(mpa);
        Film oldFilm = filmDbStorage.create(film1);
        film1.setId(oldFilm.getId());

        film1.setReleaseDate(LocalDate.parse("1980-03-03"));
        Film newFilm = filmDbStorage.update(film1);

        assertThat(filmDbStorage.findAll().get(0).getName()).isEqualTo(oldFilm.getName());
        assertThat(filmDbStorage.findAll().get(0).getDescription()).isEqualTo(oldFilm.getDescription());
        assertThat(filmDbStorage.findAll().get(0).getDuration()).isEqualTo(oldFilm.getDuration());
        assertThat(filmDbStorage.findAll().get(0).getReleaseDate()).isNotEqualTo(oldFilm.getReleaseDate());
        assertThat(filmDbStorage.findAll().get(0).getReleaseDate()).isEqualTo(film1.getReleaseDate());
    }
}
