package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    List<User> users = new ArrayList<>();

    @GetMapping
    public List<User> listAllUsers() {
        return users;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        users.add(user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        users.remove(user);
        users.add(user);
        return user;
    }

    private boolean validateUser(User user) {
        if (user.getBirthday().isAfter(LocalDateTime.now())) return false;                  // дата рождения не может быть в будущем
        if (user.getEmail().isEmpty() || !(user.getEmail().contains("@"))) return false;  // электронная почта не может быть пустой и должна содержать символ @
        if (user.getLogin().isEmpty() || user.getLogin().matches("\\*\\s\\*")) return false;
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }






        return true;
    }
}
