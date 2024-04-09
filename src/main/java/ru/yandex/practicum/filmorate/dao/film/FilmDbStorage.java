package ru.yandex.practicum.filmorate.dao.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.dao.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.exceptions.NoSuchObjectException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
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
        String newFilmToDd = "insert into FILM (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA) values(?, ?, ?, ?, ?)";
        String newFilmGenreRow = "insert into FILM_GENRE (GENRE_ID, FILM_ID) values(?, ?)";
        String currentFilmsFromDb = "select * from FILM where NAME = ?";

        int sqlResult = jdbcTemplate.update(newFilmToDd,
                            film.getName(),
                            film.getDescription(),
                            Date.valueOf(film.getReleaseDate()),
                            film.getDuration(),
                            film.getMpa().getId());

        // получаем Film с Genres == null!
        Film filmFromDd = jdbcTemplate.queryForObject(currentFilmsFromDb, this::mapRowToFilm, film.getName());
        int filmId = filmFromDd.getId();

        // обновляем таблицу FILM_GENRE, внося данные из аргумента
        if (film.getGenres() != null) {
            for (Genre oneGenre : film.getGenres()) {
                jdbcTemplate.update(newFilmGenreRow,
                        oneGenre.getId(),
                        filmId
                );
            }
        }

        // получаем Film из БД со всеми заполненными полями
        Film filmFromDb = jdbcTemplate.queryForObject(currentFilmsFromDb, this::mapRowToFilm, film.getName());

        return filmFromDb;
    }

    /**
     * Удаление записи из таблицы фильмов
     * @param film объект фильм, подлежащий удалению
     */
    @Override
    public void remove(Film film) {
        String deleFilmInDb = "delete from FILM where NAME = ?";
        jdbcTemplate.update(deleFilmInDb, film.getName());
    }

    /**
     * Обновление
     * @param film фильм
     * @return фильм
     */
    @Override
    public Film update(Film film) {
        String updateFilmInDb = "update FILM set NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA = ? where ID = ?";
        String updateFilmGenreRow = "insert into FILM_GENRE (GENRE_ID, FILM_ID) values(?, ?)";

        jdbcTemplate.update(updateFilmInDb,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );

//        String sqlFilmId = "select * from FILM where NAME = ?";
//        int filmId = jdbcTemplate.queryForObject(sqlFilmId, this::mapRowToFilm, film.getName()).getId();

        if (film.getGenres() != null) {
            for (Genre oneGenre : film.getGenres()) {
                jdbcTemplate.update(updateFilmGenreRow,
                        oneGenre.getId(),
                        film.getId()
                );
            }
        }

        return film;
    }

    /**
     * Список всех
     * @return список всех фильмов
     */
    @Override
    public List<Film> findAll() {
        String allFilmsFromDb = "select * from FILM";
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

//        if (rs.next()) {
//            Film film = new Film.Builder()
//                    .id(rs.getLong("id"))
//                    .name(rs.getString("title"))
//                    .description(rs.getString("description"))
//                    .releaseDate(rs.getDate("release_date").toLocalDate())
//                    .duration(rs.getInt("duration"))
//                    .mpa(ratingStorage.getByFilmId(id).orElse(null))
//                    .genres(genreStorage.getByFilmId(id))
//                    .build();
//
//            return Optional.of(film);}
//        } else {
//            return Optional.empty();
//        }
//
//        return this.jdbcTemplate.query(
//                sql,
//                (resultSet, rowNum) -> {
//                    Film film = new Film();
//                    film.setId(Integer.parseInt(resultSet.getString("ID")));
//                    film.setName(resultSet.getString("NAME"));
//                    film.setDescription(resultSet.getString("DESCRIPTION"));
//                    film.setReleaseDate(LocalDate.parse(resultSet.getString("RELEASE_DATE")));
//                    film.setDuration(Integer.parseInt(resultSet.getString("DURATION")));
//                    return film;
//                });
    }

    @Override
    public Film likeFilm(int filmId, int userId) {
        validateFilmById(filmId);
        validateUserId(userId);

        String sqlForLike = "insert into FILM_LIKES(USER_ID, FILM_ID) values(?, ?)";

        jdbcTemplate.update(sqlForLike, userId, filmId);

        String sqlCheckQuery = "select * from FiLM where ID = ?";
        return jdbcTemplate.queryForObject(sqlCheckQuery, this::mapRowToFilm, filmId);
    }

    @Override
    public Film deleteLike(int filmId, int userId) {
        validateFilmById(filmId);
        validateUserId(userId);

        String sqlForDeleteLike = "delete from FILM_LIKES where USER_ID = ? and FILM_ID = ?";

        jdbcTemplate.update(sqlForDeleteLike, userId, filmId);

        String sqlCheckQuery = "select * from FiLM where ID = ?";
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
            String sqlListRatedFilms = "select * from FILM where ID = ?";
            topRatedFilms.add(jdbcTemplate.queryForObject(sqlListRatedFilms, this::mapRowToFilm, filmId));
        }

        return topRatedFilms;
    }

    @Override
    public Film findWithGenre(int filmId) {
        validateFilmById(filmId);

        Film result = new Film();

        String sql = "select * from FILM where ID = ?";
        result = jdbcTemplate.queryForObject(sql, this::mapRowToFilm, filmId);

        return result;
    }

    @Override
    public List<Film> findTopRated() {
        String sql = "select * from FILM where id in (select FILM_ID  from FILM_LIKES group by FILM_ID order by count(*) desc)";

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
     * <br>Используется для проверочного вывода объека фильм, как результат работы публичных методов
     * @param rs результат запроса
     * @param rowNum число строк в результате запроса
     * @return объект фильм
     * @throws SQLException
     */
    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        Film resultFilm = new Film();

        String sqlQuery = "select * from FILM_GENRE where FILM_ID = ?";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, this::mapRowToGenres, rs.getInt("ID"));

        resultFilm.setId(rs.getInt("ID"));
        resultFilm.setName(rs.getString("NAME"));
        resultFilm.setDescription(rs.getString("DESCRIPTION"));
        resultFilm.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
        resultFilm.setDuration(rs.getInt("DURATION"));
        resultFilm.setMpa(mpaDbStorage.findById(rs.getInt("MPA")));
        resultFilm.setGenres(genres);

        return resultFilm;
    }

    private Genre mapRowToGenres(ResultSet resultSet, Integer rowNum) throws SQLException {
        return genreDbStorage.findById(resultSet.getInt("GENRE_ID"));
    }


    private void validateFilmById(int checkId) {
        String sqlIdCheck = "select ID from FILM";
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

    private void validateNewFilm(Film film) throws ValidationException {
        if (film.getName().isEmpty() || film.getName() == null) {
            log.error("Ошибка добавления нового фильма. Пустое название");
            throw new ValidationException("Ошибка добавления нового фильма. Пустое название");
        }

        if (film.getDescription().length() > 200) {
            log.error("Ошибка добавления нового фильма. Превышена максимальная длина описания");
            throw new ValidationException("Ошибка добавления нового фильма. Превышена максимальная длина описания");
        }

        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            log.error("Ошибка добавления нового фильма. Дата релиза");
            throw new ValidationException("Ошибка добавления нового фильма. Дата релиза");
        }

        if (film.getDuration() < 0) {
            log.error("Ошибка добавления нового фильма. Отрицательная продолжительность");
            throw new ValidationException("Ошибка добавления нового фильма. Отрицательная продолжительность");
        }
    }

    public List<Integer> allFilmId() {
        String sqlFilmsId = "select ID from FILM";
        return jdbcTemplate.query(
                sqlFilmsId,
                (resultSet, rowNum) -> {
                    return Integer.parseInt(resultSet.getString("ID"));
                });
    }

    public List<String> allFilmNames() {
        String sqlFilmsId = "select * from FILM";
        return jdbcTemplate.query(
                sqlFilmsId,
                (resultSet, rowNum) -> {
                    return resultSet.getString("NAME");
                });
    }
}
