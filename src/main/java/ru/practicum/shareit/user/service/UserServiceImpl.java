package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exceptions.UserConflictException;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public Optional<User> addUser(UserDto user) {

        if (isDuplicateEmail(user.getEmail())) {
            throw new UserConflictException(String.format("Email %s уже существует.", user.getEmail()));
        }
        log.info("Created: " + user);
        return userStorage.addUser(user);
    }

    @Override
    public Optional<User> updateUser(long id, UserDto user) {

        log.info("For User updated: " + user);

        User updatedUser = userStorage.getUserById(id).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь %s не найден.", user)));

        Set<String> emails = userStorage.getAllUsers().stream().map(User::getEmail).collect(Collectors.toSet());

        if (emails.contains(user.getEmail()) && (!user.getEmail().equals(updatedUser.getEmail()))) {
            throw new UserConflictException(String.format("Email %s уже существует.", user.getEmail()));
        }
        if (user.getName() != null && !updatedUser.getName().isBlank()) {
            updatedUser.setName(user.getName());
        }
        if (user.getEmail() != null && !updatedUser.getEmail().isBlank()) {
            updatedUser.setEmail(user.getEmail());
        }

        return Optional.ofNullable(updatedUser);
    }

    @Override
    public List<User> getAllUsers() {

        log.info("Users received");

        return userStorage.getAllUsers();
    }

    @Override
    public User getUserById(long id) {

        User user = userStorage.getUserById(id).orElseThrow(UserNotFoundException::new);

        log.info("User received: " + user);
        return user;
    }

    @Override
    public void deleteUser(long id) {
        log.info("User {} received to remove", id);

        String email = getUserById(id).getEmail();
        userStorage.deleteUser(id, email);
    }

    private boolean isDuplicateEmail(String userEmail) {
        Set<String> emails = userStorage.getAllUsers().stream().map(User::getEmail).collect(Collectors.toSet());
        return emails.contains(userEmail);
    }
}
