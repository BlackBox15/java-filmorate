package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NoSuchObjectException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> usersMap = new HashMap<>();
    private int userId;

    @Override
    public User create(User user) {
        validateUser(user);
        user.setId(++userId);
        usersMap.put(user.getId(), user);
        log.info("Новый пользователь добавлен.");
        return user;
    }

    @Override
    public User findUser(int userId) {
        if (usersMap.containsKey(userId)) {
            return usersMap.get(userId);
        }
        log.error("Ошибка поиска user");
        throw new NoSuchObjectException("Ошибка поиска user");
    }

    @Override
    public List<User> getFriends(int userId) {
        if (usersMap.containsKey(userId)) {
            return usersMap.get(userId).getFriends().stream().map(usersMap::get).collect(Collectors.toList());
        }
        log.info("Пользователь не найден, возвращён null");
        return null;
    }

    @Override
    public User remove(User user) {
        log.info("Удаление пользователя");
        return usersMap.remove(user.getId());
    }

    @Override
    public List<User> findAll() {
        log.info("Возвращение списка всех пользователей");
        return new ArrayList<>(usersMap.values());
    }

    @Override
    public User update(User user) {
        validateUser(user);
        if (usersMap.containsKey(user.getId())) {
            usersMap.put(user.getId(), user);
            log.info("Пользователь обновлён");
            return user;
        }
        log.error("Ошибка при обновлении user");
        throw new NoSuchObjectException("Ошибка при обновлении user");
    }

    @Override
    public User addFriend(int userId, int friendId) {
        if (usersMap.containsKey(userId) && usersMap.containsKey(friendId)) {
            usersMap.get(userId).getFriends().add(friendId);
            usersMap.get(friendId).getFriends().add(userId);
            log.info("К пользователю добавлен друг");
            return usersMap.get(friendId);
        }
        log.error("Ошибка при добавлении в друзья");
        throw new NoSuchObjectException("Ошибка при добавлении в друзья");
    }

    @Override
    public User removeFriend(int userId, int friendId) {
        User userToUpdate = usersMap.get(userId);
        User friendToRemove = usersMap.get(friendId);
        if (userToUpdate != null && friendToRemove != null) {
            userToUpdate.getFriends().remove(friendId);
            friendToRemove.getFriends().remove(userId);
            log.info("Удаление одного из друзей");
            return friendToRemove;
        }
        log.error("Ошибка при удалении из друзей");
        throw new NoSuchObjectException("Ошибка при удалении из друзей");
    }

    @Override
    public List<User> getSharedFriends(int userId, int otherId) {
        if (usersMap.containsKey(userId) && usersMap.containsKey(otherId)) {
            List<Integer> sharedUserList = usersMap.get(userId).getFriends().stream()
                    .filter(t -> usersMap.get(otherId).getFriends().contains(t))
                    .collect(Collectors.toList());
            List<User> sharedFriends = new ArrayList<>();
            for (Integer id :
                    sharedUserList) {
                sharedFriends.add(usersMap.get(id));
            }
            log.info("Возвращение списка общих друзей");
            return sharedFriends;
        }
        log.error("Ошибка при выполнении поиска общих друзей");
        throw new NoSuchObjectException("Ошибка при выполнении поиска общих друзей");
    }

    private void validateUser(User user) throws ValidationException {
        if (user.getEmail() == null || !(user.getEmail().contains("@"))) {
            log.error("Email не может быть пустым, должен содержать символ @");
            throw new ValidationException("Email не может быть пустым, должен содержать символ @");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }

        if (user.getLogin() == null) {
            log.error("Логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }

        if ((user.getName() == null)) {
            user.setName(user.getLogin());
        }
    }
}
