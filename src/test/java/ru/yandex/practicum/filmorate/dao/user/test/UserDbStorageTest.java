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
        newUser.setId(1);
        newUser.setEmail("user@email.ru");
        newUser.setName("Ivan Petrov");
        newUser.setLogin("vanya123");
        newUser.setBirthday(LocalDate.of(1990, 1, 1));
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.create(newUser);
        List<User> savedUsers = userStorage.findAll();

        // вызываем тестируемый метод
        User savedUser = userStorage.findUser(4);

        // проверяем утверждения
        assertThat(savedUser)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(newUser);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testFindAll() {
        User newUser1 = new User();
        newUser1.setId(1);
        newUser1.setEmail("user@email.ru");
        newUser1.setName("Ivan Petrov");
        newUser1.setLogin("vanya123");
        newUser1.setBirthday(LocalDate.of(1990, 1, 1));

        User newUser2 = new User();
        newUser2.setId(2);
        newUser2.setEmail("user@email.fff");
        newUser2.setName("Petr Ivanov");
        newUser2.setLogin("kkkk222");
        newUser2.setBirthday(LocalDate.of(1990, 2, 1));

        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.create(newUser1);
        userStorage.create(newUser2);

        List<User> savedUsers = userStorage.findAll();

        assertThat(savedUsers.get(0))
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(newUser1);        // и сохраненного пользователя - совпадают
        assertThat(savedUsers.get(1))
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(newUser2);        // и сохраненного пользователя - совпадают
    }


    @Test
    public void testRemoveUser() {
        User newUser1 = new User();
        newUser1.setId(1);
        newUser1.setEmail("user@email.ru");
        newUser1.setName("Ivan Petrov");
        newUser1.setLogin("vanya123");
        newUser1.setBirthday(LocalDate.of(1990, 1, 1));

        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.create(newUser1);




    }
}