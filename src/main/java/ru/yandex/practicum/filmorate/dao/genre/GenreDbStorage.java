package ru.yandex.practicum.filmorate.dao.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NoSuchObjectException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Slf4j
public class GenreDbStorage implements GenreStorage{
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> findAll() {
        String sql = "select * from GENRE order by ID asc ";

        return this.jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> {
                    Genre genre = new Genre();
                    genre.setId(Integer.parseInt(resultSet.getString("ID")));
                    genre.setName(resultSet.getString("GENRE"));
                    return genre;
                });
    }

    @Override
    public Genre findById(int genreId) {
        if (!allGenreId().contains(genreId)) {
            log.error("Отсутствует Genres");
            throw new NoSuchObjectException("Отсутствует Genres");
        }

        String sql = "select * from GENRE where ID = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowGenre, genreId);
    }

    private Genre mapRowGenre(ResultSet rs, int rowNum) throws SQLException {
        Genre result = new Genre();

        result.setId(rs.getInt("ID"));
        result.setName(rs.getString("GENRE"));

        return result;
    }

    public List<Integer> allGenreId() {
        String sqlGenres = "select ID from GENRE";
        return jdbcTemplate.query(
                sqlGenres,
                (resultSet, rowNum) -> {
                    return Integer.parseInt(resultSet.getString("ID"));
                });
    }
}
