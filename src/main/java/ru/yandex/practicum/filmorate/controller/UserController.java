package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private Map<Integer, User> usersMap = new HashMap<>();
    private int userId;

    @GetMapping
    public List<User> listAllUsers() {
        return new ArrayList<>(usersMap.values());
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        validateUser(user);

        user.setId(++userId);
        usersMap.put(user.getId(), user);
        log.info("Новый пользователь добавлен.");
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) throws ValidationException, NoSuchElementException {
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

    private boolean validateUser(User user) throws ValidationException {
        if (user.getEmail().isEmpty() || !(user.getEmail().contains("@"))) {
            log.error("электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("дата рождения не может быть в будущем");
            throw new ValidationException("дата рождения не может быть в будущем");
        }

        if (user.getLogin().isEmpty()) {
            log.error("логин не может быть пустым и содержать пробелы");
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }

        if ((user.getName() == null)) {
            user.setName(user.getLogin());
        }

        return true;
    }
}
