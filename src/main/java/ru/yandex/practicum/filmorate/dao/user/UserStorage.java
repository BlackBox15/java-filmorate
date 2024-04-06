package ru.yandex.practicum.filmorate.dao.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User create(User user);

    User remove(User user);

    User update(User user);

    User findUser(int userId);

    User addFriend(int userId, int friendId);

    User removeFriend(int userId, int friendId);

    List<User> getSharedFriends(int userId, int otherId);

    List<User> getFriends(int userId);

    List<User> findAll();
}
