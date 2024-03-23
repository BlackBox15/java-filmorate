package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) throws ValidationException, NoSuchElementException {
        return userService.update(user);
    }

    @DeleteMapping
    public User remove(@RequestBody User user) throws NullPointerException {
        return userService.remove(user);
    }

    @GetMapping
    @ResponseBody
    public List<User> findAll() {
        return userService.findAll();
    }


    @GetMapping("/{id}")
    @ResponseBody
    public User findUser(@PathVariable int id) {
        return userService.findUser(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> findAllFriends(@PathVariable int id) {
        return userService.findAllFriends(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseBody
    public User addFriend(@PathVariable int id, @PathVariable int friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseBody
    public User removeFriend(@PathVariable int id, @PathVariable int friendId) {
        return userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseBody
    public List<User> getSharedFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getSharedFriends(id, otherId);
    }
}
