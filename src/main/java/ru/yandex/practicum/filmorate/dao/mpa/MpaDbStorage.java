package ru.yandex.practicum.filmorate.dao.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NoSuchObjectException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Slf4j
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> findAll() {
        String sql = "select * from MPA order by ID";

        return this.jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> {
                    Mpa mpa = new Mpa();
                    mpa.setId(Integer.parseInt(resultSet.getString("ID")));
                    mpa.setName(resultSet.getString("RATING"));
                    return mpa;
                });
    }

    @Override
    public Mpa findById(int mpaId) {
        String sql = "select * from MPA where ID = ?";

//        if (!allMpaId().contains(mpaId)) {
//            log.error("Отсутствует MPA-рейтинг");
//            throw new NoSuchObjectException("Отсутствует MPA-рейтинг");
//        }

        return jdbcTemplate.queryForObject(sql, this::mapRowMpa, mpaId);
    }

    private Mpa mapRowMpa(ResultSet rs, int rowNum) throws SQLException {
        Mpa result = new Mpa();
        result.setId(rs.getInt("ID"));
        result.setName(rs.getString("RATING"));
        return result;
    }

    @Override
    public List<Integer> allMpaId() {
        String sqlMpa = "select ID from MPA";
        return jdbcTemplate.query(
                sqlMpa,
                (resultSet, rowNum) -> {
                    return Integer.parseInt(resultSet.getString("ID"));
                });
    }
}
