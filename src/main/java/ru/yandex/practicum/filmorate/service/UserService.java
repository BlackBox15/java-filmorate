package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.user.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;

import java.util.List;

@Service
public class UserService {
    private final UserDbStorage userDbStorage;

    public UserService(UserDbStorage userDbStorage) {
        this.userDbStorage = userDbStorage;
    }

//    public UserService(UserStorage inMemoryUserStorage) {
//        this.inMemoryUserStorage = inMemoryUserStorage;
//    }

    public User findUser(int userId) {
        return userDbStorage.findUser(userId);
    }

    public List<User> findAllFriends(int userId) {
        return userDbStorage.getFriends(userId);
    }

    public User addFriend(int userId, int friendId) {
        return userDbStorage.addFriend(userId, friendId);
    }

    public List<User> getSharedFriends(int id, int otherId) {
        return userDbStorage.getSharedFriends(id, otherId);
    }

    public User create(User user) {
        return userDbStorage.create(user);
    }

    public User update(User user) {
        return userDbStorage.update(user);
    }

    public User removeFriend(int userId, int friendId) {
        return userDbStorage.removeFriend(userId, friendId);
    }

    public List<User> findAll() {
        return userDbStorage.findAll();
    }

    public User remove(User user) {
        return userDbStorage.remove(user);
    }

}
