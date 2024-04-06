package ru.yandex.practicum.filmorate.dao.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
@Slf4j
public class FilmDbStorage implements FilmStorage{
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    /**
     * Создание новой записи в таблице пользователей.
     * @param film объект фильм
     * @return объект фильм, полученный из БД
     */
    @Override
    public Film create(Film film) {
        String sql = "insert into film(name, description, release_date, duration, rating_id) values(?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRating()
        );

        String sqlCheckQuery = "select * from USERS where LOGIN = ?";
        return jdbcTemplate.queryForObject(sqlCheckQuery, this::mapRowToUser, film.getName());
    }

    /**
     * Удаление записи из таблицы фильмов
     * @param film объект фильм, подлежащий удалению
     * @return пока непонятно что за объект фильм здесь должен быть
     */
    @Override
    public Film remove(Film film) {
        String sql = "delete from FILM where NAME = ?";

        jdbcTemplate.update(sql, film.getName());

        String sqlCheckQuery = "select * from USERS where LOGIN = ?";
        return jdbcTemplate.queryForObject(sqlCheckQuery, this::mapRowToUser, film.getName());
    }

    /**
     * Обновление
     * @param film фильм
     * @return фильм
     */
    @Override
    public Film update(Film film) {
        String sql = "update FILM set NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, RATING_ID = ? where NAME = ?";

        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRating()
        );

        String sqlCheckQuery = "select * from USERS where LOGIN = ?";
        return jdbcTemplate.queryForObject(sqlCheckQuery, this::mapRowToUser, film.getName());    }

    /**
     * Список всех
     * @return список всех фильмов
     */
    @Override
    public List<Film> findAll() {
        String sql = "select * from FILM";

        return this.jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> {
                    Film film = new Film();
                    film.setId(Integer.parseInt(resultSet.getString("ID")));
                    film.setName(resultSet.getString("NAME"));
                    film.setDescription(resultSet.getString("DESCRIPTION"));
                    film.setReleaseDate(LocalDate.parse(resultSet.getString("RELEASE_DATE")));
                    film.setDuration(Integer.parseInt(resultSet.getString("DURATION")));
                    film.setRating(Integer.parseInt(resultSet.getString("RATING_ID")));
                    return film;
                });
    }

    @Override
    public Film likeFilm(int filmId, int userId) {
        return null;
    }

    @Override
    public Film deleteLike(int filmId, int userId) {
        return null;
    }

    @Override
    public List<Film> findTopRated(int count) {
        return null;
    }

    @Override
    public List<Film> findTopRated() {
        return null;
    }

    /**
     * Формирование объекта фильм из результата запроса.
     * <br>Используется для проверочного вывода объека фильм, как результат работы публичных методов
     * @param rs результат запроса
     * @param rowNum число строк в результате запроса
     * @return объект фильм
     * @throws SQLException
     */
    private Film mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        Film resultFilm = new Film();

        resultFilm.setId(rs.getInt("ID"));
        resultFilm.setName(rs.getString("NAME"));
        resultFilm.setDescription(rs.getString("DESCRIPTION"));
        resultFilm.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
        resultFilm.setDuration(Integer.parseInt(rs.getString("DURATION")));
        resultFilm.setRating(Integer.parseInt(rs.getString("RATING_ID")));

        return resultFilm;
    }
}
