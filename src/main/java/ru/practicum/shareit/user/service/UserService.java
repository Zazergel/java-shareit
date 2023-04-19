package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAll();

    UserDto getById(Long id);

    UserDto add(UserDto userDto);

    UserDto update(Long id, UserDto userDto);

    void delete(Long id);
}
