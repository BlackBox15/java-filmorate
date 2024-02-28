package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

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
    public void create(@RequestBody User user) {
        users.add(user);
    }

    @PutMapping
    public void update(@RequestBody User user) {
        users.remove(user);
        users.add(user);
    }
}
