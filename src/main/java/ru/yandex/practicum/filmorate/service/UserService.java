package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class UserService {
    private final UserStorage inMemoryUserStorage;

    @Autowired
    public UserService(UserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public User findUser(int userId) {
        return inMemoryUserStorage.findUser(userId);
    }

    public List<User> findAllFriends(int userId) {
        return inMemoryUserStorage.getFriends(userId);
    }

    public User addFriend(int userId, int friendId) {
        return inMemoryUserStorage.addFriend(userId, friendId);
    }

    public List<User> getSharedFriends(int id, int otherId) {
        return inMemoryUserStorage.getSharedFriends(id, otherId);
    }

    public User create(User user) {
        return inMemoryUserStorage.create(user);
    }

    public User update(User user) {
        return inMemoryUserStorage.update(user);
    }

    public User removeFriend(int userId, int friendId) {
        return inMemoryUserStorage.removeFriend(userId, friendId);
    }

    public List<User> findAll() {
        return inMemoryUserStorage.findAll();
    }

    public User remove(User user) {
        return inMemoryUserStorage.remove(user);
    }

}
