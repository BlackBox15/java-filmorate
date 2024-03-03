package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/users")
public class UserController {
    List<User> users = new ArrayList<>();
    private int userId;

    @GetMapping
    public List<User> listAllUsers() {
        return users;
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        if (!validateUser(user)) {
            throw new ValidationException("Аргумент user не прошёл проверку");
        }
        user.setId(++userId);
        users.add(user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) throws ValidationException, NoSuchElementException {
        if (!validateUser(user)) {
            throw new ValidationException("Аргумент film не прошёл проверку");
        }


        return users.stream()
                .filter(item -> item.getId() == user.getId())
                .findFirst().map(item -> {
                    users.remove(item);
                    users.add(user);
                    return user;
                })
                .orElseThrow();
    }

    private boolean validateUser(User user) {
        // электронная почта не может быть пустой и должна содержать символ @
        if (user.getEmail().isEmpty() || !(user.getEmail().contains("@"))) return false;

        // дата рождения не может быть в будущем
        if (user.getBirthday().isAfter(LocalDate.now())) return false;

        // логин не может быть пустым и содержать пробелы
        if (user.getLogin().isEmpty() || user.getLogin().matches("\\*\\s\\*")) return false;

        // имя для отображения может быть пустым — в таком случае будет использован логин
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        return true;
    }
}
