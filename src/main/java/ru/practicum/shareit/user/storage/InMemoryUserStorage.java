package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users;
    private long id;

    public InMemoryUserStorage() {
        this.users = new HashMap<>();
        this.id = 1L;
    }

    @Override
    public Optional<User> addUser(UserDto user) {

        User newUser = new User(id++, user.getName(), user.getEmail());
        users.put(newUser.getId(), newUser);
        return Optional.of(newUser);
    }

    @Override
    public Optional<User> updateUser(User user) {

        return Optional.ofNullable(users.put(user.getId(), user));
    }

    @Override
    public List<User> getAllUsers() {

        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getUserById(long id) {
        if (users.containsKey(id)) {
            return Optional.ofNullable(users.get(id));
        }

        throw new UserNotFoundException("Id does not exist");
    }

    @Override
    public void deleteUser(long id, String email) {

        log.info("User {} removed", id);
        users.remove(id);
    }
}
