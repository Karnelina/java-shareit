package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> addUser(UserDto user);

    Optional<User> updateUser(User user);

    List<User> getAllUsers();

    Optional<User> getUserById(long id);

    void deleteUser(long id, String email);
}
