package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {
    User save(User user);

    User update(User userDto, Long id);

    Collection<User> findAll();

    User findById(Long id);

    void deleteById(Long id);
}
