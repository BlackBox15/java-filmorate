package ru.yandex.practicum.filmorate.dao.user.test;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
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
    private FilmDbStorage filmDbStorage;
    private Film film1;
    private Film film2;
    private Film film3;

    @BeforeEach
    public void initCase() {
        GenreDbStorage genreDbStorage = new GenreDbStorage(jdbcTemplate);
        MpaDbStorage mpaDbStorage = new MpaDbStorage(jdbcTemplate);
        filmDbStorage = new FilmDbStorage(jdbcTemplate, genreDbStorage, mpaDbStorage);

        film1 = new Film();
        film1.setName("Ну погоди");
        film1.setDuration(50);
        film1.setReleaseDate(LocalDate.parse("1980-01-01"));
        Mpa mpa = new Mpa();
        mpa.setId(3);
        film1.setMpa(mpa);

        film2 = new Film();
        film2.setName("Ну погоди 2");
        film2.setDuration(50);
        film2.setReleaseDate(LocalDate.parse("1984-01-01"));
        film2.setMpa(mpa);

        film3 = new Film();
        film3.setName("Ну погоди 3");
        film3.setDuration(87);
        film3.setReleaseDate(LocalDate.parse("1988-01-01"));
        film3.setMpa(mpa);

        Film checkedFilm1 = filmDbStorage.create(film1);
        film1.setId(checkedFilm1.getId());

        Film checkedFilm2 = filmDbStorage.create(film2);
        film2.setId(checkedFilm2.getId());

        Film checkedFilm3 = filmDbStorage.create(film3);
        film3.setId(checkedFilm3.getId());
    }

    @Test
    public void testAllFilmIds() {
        List<Integer> actualIds = List.of(film1.getId(), film2.getId(), film3.getId());
        List<Integer> checkedIds = filmDbStorage.allFilmId();

        assertThat(checkedIds).isEqualTo(actualIds);
    }

    @Test
    public void testAllFilmNames() {
        List<String> actualFilmNames = List.of(film1.getName(), film2.getName(), film3.getName());
        List<String> checkedFilmNames = filmDbStorage.allFilmNames();

        assertThat(checkedFilmNames).isEqualTo(actualFilmNames);
    }

    @Test
    public void testCreateFilm() {
        Film anotherFilm = new Film();
        anotherFilm.setName("Какой-то странный фильм, ужастик, и это его название");
        anotherFilm.setDuration(187);
        anotherFilm.setReleaseDate(LocalDate.parse("2000-01-01"));
        Mpa mpa = new Mpa();
        mpa.setId(5);
        anotherFilm.setMpa(mpa);

        int oldSizeDbStorage = filmDbStorage.findAll().size();

        Film checkedFilm1 = filmDbStorage.create(anotherFilm);
        anotherFilm.setId(checkedFilm1.getId());

        assertThat(filmDbStorage.findAll().size()).isEqualTo(oldSizeDbStorage + 1);
        assertThat(filmDbStorage.findAll().contains(anotherFilm)).isTrue();
    }

    @Test
    public void testUpdateFilm() {
        Film filmToUpdate = filmDbStorage.findAll().get(filmDbStorage.findAll().size() - 1);
        filmToUpdate.setName("Обновлённое название");
        filmDbStorage.update(filmToUpdate);

        assertThat(filmDbStorage.findAll().contains(filmToUpdate)).isTrue();
        assertThat(filmDbStorage.findAll().stream().anyMatch(filmToUpdate::equals)).isTrue();
    }

    @Test
    public void testDeleteFilm() {
        Film filmToDelete = filmDbStorage.findAll().get(filmDbStorage.findAll().size() - 1);
        assertThat(filmDbStorage.findAll().contains(filmToDelete)).isTrue();
        filmDbStorage.remove(filmToDelete);
        assertThat(filmDbStorage.findAll().contains(filmToDelete)).isFalse();
    }

    @Test
    public void testFindAllFilms() {
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


    }
}
