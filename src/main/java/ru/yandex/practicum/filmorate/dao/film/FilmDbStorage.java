package ru.yandex.practicum.filmorate.dao.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.dao.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.exceptions.NoSuchObjectException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreDbStorage genreDbStorage;
    private final MpaDbStorage mpaDbStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreDbStorage genreDbStorage, MpaDbStorage mpaDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDbStorage = genreDbStorage;
        this.mpaDbStorage = mpaDbStorage;
    }

    /**
     * Создание новой записи в таблице пользователей.
     * @param film объект фильма
     * @return объект фильм, полученный из БД
     */
    @Override
    public Film create(Film film) {
        String newFilmToDd = "insert into FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA) values(?, ?, ?, ?, ?)";
        String newFilmGenreRow = "insert into FILM_GENRE (GENRE_ID, FILM_ID) values(?, ?)";
        String currentFilmsFromDb = "select * from FILMS where NAME = ? order by ID desc limit 1";

        jdbcTemplate.update(newFilmToDd,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId());

        Film filmFromDd = jdbcTemplate.queryForObject(currentFilmsFromDb, this::mapRowToFilm, film.getName());
        int filmId = filmFromDd.getId();

        if (film.getGenres() != null) {
            for (Genre oneGenre : film.getGenres()) {
                jdbcTemplate.update(newFilmGenreRow, oneGenre.getId(), filmId);
            }
        }

        Film filmFromDb = jdbcTemplate.queryForObject(currentFilmsFromDb, this::mapRowToFilm, film.getName());
        film.setId(filmFromDb.getId());

        return filmFromDb;
    }

    /**
     * Удаление записи из таблицы фильмов
     * @param film объект фильм, подлежащий удалению
     */
    @Override
    public void remove(Film film) {
        String deleFilmInDb = "delete from FILMS where NAME = ?";
        jdbcTemplate.update(deleFilmInDb, film.getName());
    }

    /**
     * Обновление
     * @param film фильм
     * @return фильм
     */
    @Override
    public Film update(Film film) {
        String updateFilmInDb = "update FILMS set NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA = ? where ID = ?";
        String updateFilmGenreRow = "insert into FILM_GENRE (GENRE_ID, FILM_ID) values(?, ?)";
        String deleteOldFilmGenreRow = "delete from FILM_GENRE where FILM_ID = ?";
        String currentFilmsFromDb = "select * from FILMS where NAME = ?";

        jdbcTemplate.update(deleteOldFilmGenreRow, film.getId());

        if (film.getGenres() != null) {
            for (Genre oneGenre : film.getGenres()) {
                jdbcTemplate.update(updateFilmGenreRow, oneGenre.getId(), film.getId());
            }
        }

        jdbcTemplate.update(updateFilmInDb,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );

        Film filmFromDb = jdbcTemplate.queryForObject(currentFilmsFromDb, this::mapRowToFilm, film.getName());

        return filmFromDb;
    }

    /**
     * Получение списка всех фильмов
     * @return список всех фильмов
     */
    @Override
    public List<Film> findAll() {
        String allFilmsFromDb = "select * from FILMS";
        String genresForFilmFromDb = "select * from GENRE where ID in (select GENRE_ID from FILM_GENRE where FILM_ID = ?)";
        List<Film> films = new ArrayList<>();

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(allFilmsFromDb);

        while (filmRows.next()) {
            Film film = new Film();
            film.setId(filmRows.getInt("ID"));
            film.setName(filmRows.getString("NAME"));
            film.setDescription(filmRows.getString("DESCRIPTION"));
            film.setReleaseDate(filmRows.getDate("RELEASE_DATE").toLocalDate());
            film.setDuration(filmRows.getInt("DURATION"));
            film.setMpa(mpaDbStorage.findById(filmRows.getInt("MPA")));
            film.setGenres(jdbcTemplate.query(
                    genresForFilmFromDb,
                    (resultSet, rowNum) -> {
                        Genre genre = new Genre();
                        genre.setName(resultSet.getString("GENRE"));
                        genre.setId(Integer.parseInt(resultSet.getString("ID")));
                        return genre;
                    },
                    filmRows.getInt("ID")));
            films.add(film);
        }

        return films;
    }

    @Override
    public Film likeFilm(int filmId, int userId) {
        validateFilmById(filmId);
        validateUserId(userId);

        String sqlForLike = "insert into FILM_LIKES(USER_ID, FILM_ID) values(?, ?)";

        jdbcTemplate.update(sqlForLike, userId, filmId);

        String sqlCheckQuery = "select * from FiLMS where ID = ?";
        return jdbcTemplate.queryForObject(sqlCheckQuery, this::mapRowToFilm, filmId);
    }

    @Override
    public Film deleteLike(int filmId, int userId) {
        validateFilmById(filmId);
        validateUserId(userId);

        String sqlForDeleteLike = "delete from FILM_LIKES where USER_ID = ? and FILM_ID = ?";

        jdbcTemplate.update(sqlForDeleteLike, userId, filmId);

        String sqlCheckQuery = "select * from FILMS where ID = ?";
        return jdbcTemplate.queryForObject(sqlCheckQuery, this::mapRowToFilm, filmId);
    }

    @Override
    public List<Film> findTopRated(int count) {
        String sqlRatedFilms = "select FILM_ID from FILM_LIKES group by FILM_ID order by count(*) desc limit ?";
        List<Integer> ratedFilms = jdbcTemplate.query(
                sqlRatedFilms,
                (resultSet, rowNum) -> {
                    return resultSet.getInt("FILM_ID");
                }, count);

        List<Film> topRatedFilms = new ArrayList<>();
        for (Integer filmId : ratedFilms) {
            String sqlListRatedFilms = "select * from FILMS where ID = ?";
            topRatedFilms.add(jdbcTemplate.queryForObject(sqlListRatedFilms, this::mapRowToFilm, filmId));
        }

        return topRatedFilms;
    }

    @Override
    public Film findWithGenre(int filmId) {
        String getFilmFromDb = "select * from FILMS where ID = ?";

        Film result = new Film();

        result = jdbcTemplate.queryForObject(getFilmFromDb, this::mapRowToFilm, filmId);

        return result;
    }

    @Override
    public List<Film> findTopRated() {
        String sql = "select * from FILMS where id in (select FILM_ID  from FILM_LIKES group by FILM_ID order by count(*) desc)";

        return this.jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> {
                    Film film = new Film();
                    film.setId(Integer.parseInt(resultSet.getString("ID")));
                    film.setName(resultSet.getString("NAME"));
                    film.setDescription(resultSet.getString("DESCRIPTION"));
                    film.setReleaseDate(LocalDate.parse(resultSet.getString("RELEASE_DATE")));
                    film.setDuration(Integer.parseInt(resultSet.getString("DURATION")));
                    return film;
                });
    }

    /**
     * Формирование объекта фильм из результата запроса.
     * @param rs результат запроса
     * @param rowNum число строк в результате запроса
     * @return объект фильм
     * @throws SQLException
     */
    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        Film resultFilm = new Film();

        String sqlQuery = "select * from FILM_GENRE where FILM_ID = ?";
        List<Genre> genres = new ArrayList<>();
        genres = jdbcTemplate.query(sqlQuery, (resultSet, rN) -> mapRowToGenre(resultSet, rN), rs.getInt("ID"));

        resultFilm.setId(rs.getInt("ID"));
        resultFilm.setName(rs.getString("NAME"));
        resultFilm.setDescription(rs.getString("DESCRIPTION"));
        resultFilm.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
        resultFilm.setDuration(rs.getInt("DURATION"));
        resultFilm.setMpa(mpaDbStorage.findById(rs.getInt("MPA")));
        resultFilm.setGenres(genres);

        return resultFilm;
    }

    private Genre mapRowToGenre(ResultSet resultSet, Integer rowNum) throws SQLException {
        int genreId = resultSet.getInt("GENRE_ID");
        if (genreId == 0) {
            return null;
        }
        return genreDbStorage.findById(genreId);
    }

    private void validateFilmById(int checkId) {
        String sqlIdCheck = "select ID from FILMS";
        List<Integer> ids = jdbcTemplate.query(sqlIdCheck, (rs, rowNum) -> (Integer.parseInt(rs.getString("ID"))));

        if (!ids.contains(checkId)) {
            throw new NoSuchObjectException("Фильм с ID не найден в БД.");
        }
    }

    private void validateUserId(int userId) {
        String sqlIdCheck = "select ID from USERS";
        List<Integer> ids = jdbcTemplate.query(sqlIdCheck, (rs, rowNum) -> (Integer.parseInt(rs.getString("ID"))));

        if (!ids.contains(userId)) {
            throw new NoSuchObjectException("Пользователь с ID не найден в БД.");
        }
    }

    public List<Integer> allFilmId() {
        String sqlFilmsId = "select ID from FILMS";
        return jdbcTemplate.query(
                sqlFilmsId,
                (resultSet, rowNum) -> {
                    return Integer.parseInt(resultSet.getString("ID"));
                });
    }

    public List<String> allFilmNames() {
        String sqlFilmsId = "select * from FILMS";
        return jdbcTemplate.query(
                sqlFilmsId,
                (resultSet, rowNum) -> {
                    return resultSet.getString("NAME");
                });
    }
}
