package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.List;

@Service
public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    /**
     * Создайте UserService, который будет отвечать за такие операции с пользователями, как
     *  добавление в друзья,
     *  удаление из друзей,
     *  вывод списка общих друзей.
     *
     * Пока пользователям не надо одобрять заявки в друзья — добавляем сразу.
     * То есть если Лена стала другом Саши, то это значит, что Саша теперь друг Лены.
     */

    public List<User> findAll() {
        return inMemoryUserStorage.findAll();
    }

    public User create(User user) {
        return inMemoryUserStorage.create(user);
    }

    public User remove(User user) {
        return inMemoryUserStorage.remove(user);
    }
}
