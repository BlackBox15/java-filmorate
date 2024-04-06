package ru.yandex.practicum.filmorate.dao.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
public class UserDbStorage implements UserStorage{
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        // alter table
        String sql_old = "select * from cat_post where author_id = ? order by creation_date desc";
        String sql = "insert into users(email, login, name, birthday) values(?, ?, ?, ?)";

//        return jdbcTemplate.query(sql, (rs, rowNum) -> makePost(user, rs), user.getId());
        return null;
    }

    @Override
    public User remove(User user) {
        String sql = "select * from cat_post where author_id = ? order by creation_date desc";
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public User findUser(int userId) {
        String sql = "select * from cat_post where author_id = ? order by creation_date desc";
        return null;
    }

    @Override
    public User addFriend(int userId, int friendId) {
        return null;
    }

    @Override
    public User removeFriend(int userId, int friendId) {
        return null;
    }

    @Override
    public List<User> getSharedFriends(int userId, int otherId) {
        return null;
    }

    @Override
    public List<User> getFriends(int userId) {
        return null;
    }

    @Override
    public List<User> findAll() {
        String sql = "select * from users";

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
}
