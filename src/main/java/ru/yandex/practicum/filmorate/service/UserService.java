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

    public User findUser(Long userId) {
        return inMemoryUserStorage.findUser(userId);
    }

    public List<User> findAllFriends(Long userId) {
        return inMemoryUserStorage.getFriends(userId);
    }

    public User addFriend(Long userId, Long friendId) {
        return inMemoryUserStorage.addFriend(userId, friendId);
    }

    public List<User> getSharedFriends(Long id, Long otherId) {
        return inMemoryUserStorage.getSharedFriends(id, otherId);
    }

    public User create(User user) {
        return inMemoryUserStorage.create(user);
    }

    public User update(User user) {
        return inMemoryUserStorage.update(user);
    }

    public User removeFriend(Long userId, Long friendId) {
        return inMemoryUserStorage.removeFriend(userId, friendId);
    }

    public List<User> findAll() {
        return inMemoryUserStorage.findAll();
    }

    public User remove(User user) {
        return inMemoryUserStorage.remove(user);
    }

}
