package ru.yandex.practicum.filmorate.dao.user.test;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.user.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testFindUserById() {
        // Подготавливаем данные для теста
        User newUser = new User();
        newUser.setId(0);
        newUser.setEmail("user@email.ru");
        newUser.setName("Ivan Petrov");
        newUser.setLogin("vanya123");
        newUser.setBirthday(LocalDate.of(1990, 1, 1));
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.create(newUser);

        List<User> expectedUsers = userStorage.findAll();

        for (User user : expectedUsers) {
            // проверяем утверждения
            user.setId(newUser.getId());
            assertThat(user)
                    .isNotNull() // проверяем, что объект не равен null
                    .usingRecursiveComparison() // проверяем, что значения полей нового
                    .isEqualTo(newUser);        // и сохраненного пользователя - совпадают
        }
    }

    @Test
    public void testFindAll() {
        User newUser1 = new User();
        newUser1.setEmail("user@email.ru");
        newUser1.setName("Ivan Petrov");
        newUser1.setLogin("vanya123");
        newUser1.setBirthday(LocalDate.of(1990, 1, 1));

        User newUser2 = new User();
        newUser2.setEmail("user@email.fff");
        newUser2.setName("Petr Ivanov");
        newUser2.setLogin("kkkk222");
        newUser2.setBirthday(LocalDate.of(1990, 2, 1));

        List<User> actualUsers = List.of(newUser1, newUser2);

        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        newUser1.setId(userStorage.create(newUser1).getId());
        newUser2.setId(userStorage.create(newUser2).getId());

        List<User> expectedUsers = userStorage.findAll();

        for (int i = 0; i < 2; i++) {
            assertThat(expectedUsers.get(i))
                    .isNotNull() // проверяем, что объект не равен null
                    .usingRecursiveComparison() // проверяем, что значения полей нового
                    .isEqualTo(actualUsers.get(i));        // и сохраненного пользователя - совпадают
        }
    }


    @Test
    public void testRemoveUser() {
        User newUser1 = new User();
        newUser1.setEmail("user@email.ru");
        newUser1.setName("Ivan Petrov");
        newUser1.setLogin("vanya123");
        newUser1.setBirthday(LocalDate.of(1990, 1, 1));

        User newUser2 = new User();
        newUser2.setEmail("user@email.fff");
        newUser2.setName("Petr Ivanov");
        newUser2.setLogin("kkkk222");
        newUser2.setBirthday(LocalDate.of(1990, 2, 1));

        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);

        newUser1.setId(userStorage.create(newUser1).getId());
        newUser2.setId(userStorage.create(newUser2).getId());

        List<User> expectedUsers = userStorage.findAll();

        userStorage.remove(newUser1);

        assertThat(userStorage.findAll().size()).isEqualTo(1);
        for (User oneUser : userStorage.findAll()) {
            assertThat(oneUser).isEqualTo(newUser2);
        }
    }

    @Test
    public void testUpdateUser() {
        User oldUser = new User();
        oldUser.setEmail("user@email.ru");
        oldUser.setName("Ivan Petrov");
        oldUser.setLogin("vanya123");
        oldUser.setBirthday(LocalDate.of(1990, 1, 1));

        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        oldUser.setId(userStorage.create(oldUser).getId());

        oldUser.setEmail("user@email.fff");
        oldUser.setName("Petr Ivanov");
        oldUser.setLogin("vanya123");
        oldUser.setBirthday(LocalDate.of(1978, 1, 1));

        User newUser = userStorage.update(oldUser);

        assertThat(newUser.getName()).isEqualTo(oldUser.getName());
        assertThat(newUser.getBirthday()).isEqualTo(oldUser.getBirthday());
        assertThat(newUser.getLogin()).isEqualTo(oldUser.getLogin());
        assertThat(newUser.getEmail()).isEqualTo(oldUser.getEmail());
        assertThat(newUser.getId()).isEqualTo(oldUser.getId());
    }
}