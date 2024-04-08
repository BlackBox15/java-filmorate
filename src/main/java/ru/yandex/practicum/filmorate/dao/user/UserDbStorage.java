package ru.yandex.practicum.filmorate.dao.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NoSuchObjectException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
@Slf4j
public class UserDbStorage implements UserStorage{
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Создание новой записи в таблице пользователей.
     * @param user объект пользователя
     * @return объект пользователя, полученный из БД
     */
    @Override
    public User create(User user) {
        validateUser(user);

        String sql = "insert into users(email, login, name, birthday) values(?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday().toString()
                );

        String sqlCheckQuery = "select * from USERS where LOGIN = ?";
        return jdbcTemplate.queryForObject(sqlCheckQuery, this::mapRowToUser, user.getLogin());
    }

    /**
     * Удаление записи из таблицы пользователей
     * @param user объект пользователя, который требуется удалить из таблицы
     * @return пока непонятно, что за пользователя мы должны здесь вернуть
     */
    @Override
    public User remove(User user) {
        validateUserId(user.getId());

        String sql = "delete from USERS where LOGIN = ?";

        jdbcTemplate.update(sql, user.getLogin());

        String sqlCheckQuery = "select * from USERS where LOGIN = ?";
        return jdbcTemplate.queryForObject(sqlCheckQuery, this::mapRowToUser, user.getLogin());
    }

    /**
     * Обновление записи в таблице пользователей.
     * <br>Первоначальная проверка наличия пользователя с определённым ID в БД.
     * @param user объект пользователя
     * @return объект пользователя, полученный из БД
     */
    @Override
    public User update(User user) {
        validateUserId(user.getId());

        validateUser(user);

        String sql = "update USERS set EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? where ID = ?";

        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        String sqlCheckQuery = "select * from USERS where LOGIN = ?";
        return jdbcTemplate.queryForObject(sqlCheckQuery, this::mapRowToUser, user.getLogin());
    }

    /**
     * Проверка наличия данного Id в БД USERS.
     * @param userId проверяемый Id
     */
    private void validateUserId(int userId) {
        String sqlIdCheck = "select ID from USERS";
        List<Integer> ids = jdbcTemplate.query(sqlIdCheck, (rs, rowNum) -> (Integer.parseInt(rs.getString("ID"))));

        if (!ids.contains(userId)) {
            throw new NoSuchObjectException("Пользователь с ID не найден в БД.");
        }
    }

    /**
     * Поиск пользователя по ID
     * @param userId ID пользователя
     * @return объект найденного пользователя
     */
    @Override
    public User findUser(int userId) {
        String sql = "select * from USERS where id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToUser, userId);
    }

    /**
     * Добавление друга пользователя в список друзей
     * @param userId ID пользователя
     * @param friendId ID друга
     * @return пока непонятно какого пользователя требуется здесь предоставить
     */
    @Override
    public User addFriend(int userId, int friendId) {
        
        validateUserId(userId);
        validateUserId(friendId);

        String sqlAddFriend = "insert into FRIENDSHIP(USER_ID, FRIEND_ID) values (?, ?) ";
        jdbcTemplate.update(sqlAddFriend, userId, friendId);

        String sqlCheckQuery = "select * from USERS where ID = ?";
        return jdbcTemplate.queryForObject(sqlCheckQuery, this::mapRowToUser, userId);
    }

    /**
     * Удаление друга из списка друзей пользователя.
     * @param userId ID пользователя, у которого требуется удалить друга из списка
     * @param friendId ID друга, которого требуется удалить из списка друзей
     * @return User объект пользователя
     */
    @Override
        public User removeFriend(int userId, int friendId) {

        validateUserId(userId);
        validateUserId(friendId);

        String sql = "delete from FRIENDSHIP where USER_ID = ? and FRIEND_ID = ?";
        jdbcTemplate.update(sql, userId, friendId);

        String sqlCheckQuery = "select * from USERS where ID = ?";
        return jdbcTemplate.queryForObject(sqlCheckQuery, this::mapRowToUser, userId);
    }

    /**
     * Получение списка взаимных друзей
     *
     * @param userId ID пользователя
     * @param otherId ID другого пользователя
     * @return список друзей, присутствующих у обоих пользователей
     */
    @Override
        public List<User> getCommonFriends(int userId, int otherId) {
        validateUserId(userId);
        validateUserId(otherId);

        String sql = "select * from USERS where id in (select fr1.FRIEND_ID from FRIENDSHIP as fr1 join FRIENDSHIP as fr2 on fr1.FRIEND_ID = fr2.FRIEND_ID where fr1.USER_ID = ? and fr2.USER_ID = ?)";

        List<User> commonFriends = this.jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> {
                    User user = new User();
                    user.setId(Integer.parseInt(resultSet.getString("ID")));
                    user.setEmail(resultSet.getString("EMAIL"));
                    user.setLogin(resultSet.getString("LOGIN"));
                    user.setName(resultSet.getString("NAME"));
                    user.setBirthday(LocalDate.parse(resultSet.getString("BIRTHDAY")));
                    return user;
                },
                userId,
                otherId);

        return commonFriends;
    }

    /**
     * Получение списка друзей для пользователя с ID
     * @param userId ID пользователя
     * @return список друзей
     */
    @Override
    public List<User> getFriends(int userId) {

        validateUserId(userId);

        String sqlGetFriends = "select * from USERS where ID in (select FRIEND_ID from FRIENDSHIP where USER_ID = ?)";


        List<User> friends = this.jdbcTemplate.query(
                sqlGetFriends,
                (resultSet, rowNum) -> {
                    User user = new User();
                    user.setId(Integer.parseInt(resultSet.getString("ID")));
                    user.setEmail(resultSet.getString("EMAIL"));
                    user.setLogin(resultSet.getString("LOGIN"));
                    user.setName(resultSet.getString("NAME"));
                    user.setBirthday(LocalDate.parse(resultSet.getString("BIRTHDAY")));
                    return user;
                },
                userId);
        return friends;
    }

    /**
     * Вывод всех пользователей
     * @return список всех пользователей из таблицы
     */
    @Override
    public List<User> findAll() {
        String sql = "select * from USERS order by ID";

        return this.jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> {
                    User user = new User();
                    user.setId(Integer.parseInt(resultSet.getString("ID")));
                    user.setEmail(resultSet.getString("EMAIL"));
                    user.setLogin(resultSet.getString("LOGIN"));
                    user.setName(resultSet.getString("NAME"));
                    user.setBirthday(LocalDate.parse(resultSet.getString("BIRTHDAY")));
                    return user;
                });
    }

    /**
     * Формирование объекта пользователя из результата запроса.
     * <br>Используется для проверочного вывода объека пользователя, как результат работы публичных методов
     * @param rs результат запроса
     * @param rowNum число строк в результате запроса
     * @return объект пользователя
     * @throws SQLException выбрасывается исключение
     */
    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        User resultUser = new User();

        resultUser.setId(rs.getInt("ID"));
        resultUser.setEmail(rs.getString("EMAIL"));
        resultUser.setLogin(rs.getString("LOGIN"));
        resultUser.setName(rs.getString("NAME"));
        resultUser.setBirthday(rs.getDate("BIRTHDAY").toLocalDate());

        return resultUser;
    }

    /**
     * Первоначальная проверка аргумента
     * @param user объект для проверки
     * @throws ValidationException выброс исключения при наличии ошибки в аргументе
     */
    private void validateUser(User user) throws ValidationException {


        if (user.getEmail() == null || !(user.getEmail().contains("@"))) {
            log.error("Email не может быть пустым, должен содержать символ @");
            throw new ValidationException("Email не может быть пустым, должен содержать символ @");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }

        if (user.getLogin() == null || user.getLogin().contains(" ")) {
            log.error("Логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }

        if ((user.getName() == null)) {
            user.setName(user.getLogin());
        }
    }
}
