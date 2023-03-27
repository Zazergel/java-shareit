package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    User add(User user);

    User get(Long userId);

    List<User> getAll();

    User update(User userForUpdate);

    String delete(Long userId);
}