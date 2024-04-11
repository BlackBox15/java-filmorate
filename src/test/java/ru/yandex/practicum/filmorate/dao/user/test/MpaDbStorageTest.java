package ru.yandex.practicum.filmorate.dao.user.test;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testAllMpaIds() {
        List<Integer> checkList = List.of(1, 2, 3, 4, 5);
        MpaDbStorage mpaDbStorage = new MpaDbStorage(jdbcTemplate);
        List<Integer> allMpaIds =  mpaDbStorage.allMpaId();

        assertThat(checkList.containsAll(allMpaIds));
        assertThat(allMpaIds.containsAll(checkList));
    }

    @Test
    public void testAllMpa() {
        MpaDbStorage mpaDbStorage = new MpaDbStorage(jdbcTemplate);
        List<Mpa> allMpa = mpaDbStorage.findAll();
        assertThat(allMpa.size()).isEqualTo(5);
    }

    @Test
    public void testFindById() {
        Mpa actualMpa = new Mpa();
        actualMpa.setName("PG-13");
        actualMpa.setId(3);
        MpaDbStorage mpaDbStorage = new MpaDbStorage(jdbcTemplate);
        Mpa checkMpa = mpaDbStorage.findById(3);
        assertThat(actualMpa).isEqualTo(checkMpa);
    }
}
