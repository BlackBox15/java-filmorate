package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @GetMapping
    public List<User> listAllUsers() {
        return inMemoryUserStorage.listAllUsers();
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        return inMemoryUserStorage.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) throws ValidationException, NoSuchElementException {
        return inMemoryUserStorage.update(user);
    }

//    @DeleteMapping
//    public User remove(@) {
//        return inMemoryUserStorage.remove(user);
//    }
}
