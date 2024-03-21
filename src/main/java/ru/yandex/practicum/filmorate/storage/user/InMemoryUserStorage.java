package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> usersMap = new HashMap<>();
    private int userId;

    @Override
    public User create(User user) {
        validateUser(user);
        user.setId(++userId);
        usersMap.put(user.getId(), user);
        log.info("Новый пользователь добавлен.");
        return user;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(usersMap.values());
    }

    @Override
    public User remove(User user) {
        return usersMap.remove(user.getId());
    }

    @Override
    public User update(User user) {
        validateUser(user);
        if (usersMap.remove(user.getId()) != null) {
            usersMap.put(user.getId(), user);
            log.info("Фильм обновлён");
            return user;
        } else {
            log.error("Попытка обновления пользователя с несуществующим Id");
            throw new ValidationException("Попытка обновления пользователя с несуществующим Id");
        }

    }

    private void validateUser(User user) throws ValidationException {
        if (user.getEmail() == null || !(user.getEmail().contains("@"))) {
            log.error("электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("дата рождения не может быть в будущем");
            throw new ValidationException("дата рождения не может быть в будущем");
        }

        if (user.getLogin() == null) {
            log.error("логин не может быть пустым и содержать пробелы");
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }

        if ((user.getName() == null)) {
            user.setName(user.getLogin());
        }
    }
}
