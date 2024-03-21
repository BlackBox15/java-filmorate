package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User create(User user);
    User remove(User user);
    User update(User user);

    User findUser(Long userId);

    User addFriend(Long userId, Long friendId);

    User removeFriend(Long userId, Long friendId);

    List<User> getSharedFriends(Long userId, Long otherId);

    List<User> getFriends(Long userId);

    List<User> findAll();
}
