package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collections;

public class UserControllerTests {
    UserController userController;

    @Test
    public void shouldBeEmptyLists() {
        Assertions.assertEquals(Collections.EMPTY_LIST, userController.findAll());
    }

    @Test
    public void shouldCreateNewUser() {
        User testUser = new User();
        testUser.setName("Jonny");
        testUser.setBirthday(LocalDate.parse("1980-01-01"));
        testUser.setLogin("Razrushitel3000");
        testUser.setEmail("test@test.ru");

        Assertions.assertEquals(testUser, userController.create(testUser));
    }

    @Test
    public void shouldCreateUserWithoutName() {
        User testUser = new User();
        testUser.setBirthday(LocalDate.parse("1980-01-01"));
        testUser.setLogin("Razrushitel3000");
        testUser.setEmail("test@test.ru");

        Assertions.assertEquals(testUser.getLogin(), userController.create(testUser).getName());
    }

    @Test
    public void shouldThrowExceptionWithoutEmail() {
        User testUser = new User();
        testUser.setName("Jonny");
        testUser.setBirthday(LocalDate.parse("1980-01-01"));
        testUser.setLogin("Razrushitel3000");

        String exceptionMessage = "электронная почта не может быть пустой и должна содержать символ @";

        Exception exception = Assertions.assertThrows(ValidationException.class, () ->
            userController.create(testUser));
        Assertions.assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWithoutAtSymb() {
        User testUser = new User();
        testUser.setBirthday(LocalDate.parse("1980-01-01"));
        testUser.setLogin("Razrushitel3000");
        testUser.setEmail("test_test.ru");

        String exceptionMessage = "электронная почта не может быть пустой и должна содержать символ @";

        Exception exception = Assertions.assertThrows(ValidationException.class, () ->
                userController.create(testUser));
        Assertions.assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWithFutureBirthday() {
        User testUser = new User();
        testUser.setBirthday(LocalDate.parse("2300-01-01"));
        testUser.setLogin("Razrushitel3000");
        testUser.setEmail("test@test.ru");

        String exceptionMessage = "дата рождения не может быть в будущем";

        Exception exception = Assertions.assertThrows(ValidationException.class, () ->
                userController.create(testUser));
        Assertions.assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWithoutLogin() {
        User testUser = new User();
        testUser.setBirthday(LocalDate.parse("1989-01-01"));
        testUser.setEmail("test@test.ru");

        String exceptionMessage = "логин не может быть пустым и содержать пробелы";

        Exception exception = Assertions.assertThrows(ValidationException.class, () ->
                userController.create(testUser));
        Assertions.assertEquals(exceptionMessage, exception.getMessage());
    }
}
