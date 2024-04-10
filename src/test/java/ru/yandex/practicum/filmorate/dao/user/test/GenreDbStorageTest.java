package ru.yandex.practicum.filmorate.dao.user.test;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testAllGenreIds() {
        List<Integer> checkList = List.of(1, 2, 3, 4, 5, 6);
        GenreDbStorage genreDbStorage = new GenreDbStorage(jdbcTemplate);
        List<Integer> allGenreIds =  genreDbStorage.allGenreId();

        assertThat(checkList.containsAll(allGenreIds));
        assertThat(allGenreIds.containsAll(checkList));
    }

    @Test
    public void testAllGenres() {
        GenreDbStorage genreDbStorage = new GenreDbStorage(jdbcTemplate);
        List<Genre> allGenres = genreDbStorage.findAll();
        assertThat(allGenres.size()).isEqualTo(6);
    }

    @Test
    public void testFindById() {
        Genre actualGenre = new Genre();
        actualGenre.setName("Документальный");
        actualGenre.setId(5);

        GenreDbStorage genreDbStorage = new GenreDbStorage(jdbcTemplate);
        Genre checkGenre = genreDbStorage.findById(5);
        assertThat(actualGenre).isEqualTo(checkGenre);
    }
}
