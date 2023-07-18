package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User save(User user);

    User update(long id, User userDto);

    List<User> findAll();

    User findById(long id);

    void deleteById(long id);
}
