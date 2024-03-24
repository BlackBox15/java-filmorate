package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        log.debug("Получен запрос POST на добавление нового пользователя");
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) throws ValidationException, NoSuchElementException {
        log.debug("Получен запрос PUT на обновление пользователя");
        return userService.update(user);
    }

    @DeleteMapping
    public User remove(@RequestBody User user) throws NullPointerException {
        log.debug("Получен запрос DELETE на удаление пользователя");
        return userService.remove(user);
    }

    @GetMapping
    @ResponseBody
    public List<User> findAll() {
        log.debug("Получен запрос GET на получение списка всех пользователей");
        return userService.findAll();
    }


    @GetMapping("/{id}")
    @ResponseBody
    public User findUser(@PathVariable int id) {
        log.debug("Получен запрос GET на получение пользователя с id {}", id);
        return userService.findUser(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> findAllFriends(@PathVariable int id) {
        log.debug("Получен запрос GET на получение списка всех друзей пользователя с id {}", id);
        return userService.findAllFriends(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseBody
    public User addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.debug("Получен запрос PUT на добавление друга с id {} пользователю с id {}", friendId, id);
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseBody
    public User removeFriend(@PathVariable int id, @PathVariable int friendId) {
        log.debug("Получен запрос DELETE на удаление друга с id {} у пользователя с id {}", friendId, id);
        return userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseBody
    public List<User> getSharedFriends(@PathVariable int id, @PathVariable int otherId) {
        log.debug("Получен запрос GET на получение списка общих друзей пользователя с id {} и другого пользователя с id {}", id, otherId);
        return userService.getSharedFriends(id, otherId);
    }
}
