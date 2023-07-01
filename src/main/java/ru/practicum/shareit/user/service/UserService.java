package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> addUser(UserDto user);

    Optional<User> updateUser(long id, UserDto userDto);

    List<User> getAllUsers();

    User getUserById(long id);

    void deleteUser(long id);
}
