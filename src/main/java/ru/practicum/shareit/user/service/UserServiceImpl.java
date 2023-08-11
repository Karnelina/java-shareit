package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User save(User user) {

        try {

            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {

            throw new AlreadyExistsException(String.format(
                    "User %s is exist", user.getEmail()
            ));
        }
    }

    @Override
    public User update(long id, User user) {

        log.info("For User updated: " + user);

        User updatedUser = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("User %s not found.", user)));

        if (user.getName() != null && !updatedUser.getName().isBlank()) {
            updatedUser.setName(user.getName());
        }
        if (user.getEmail() != null && !updatedUser.getEmail().isBlank()) {
            updatedUser.setEmail(user.getEmail());
        }

        try {
            return userRepository.save(updatedUser);
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistsException(String.format(
                    "User %s is exist", updatedUser.getEmail()
            ));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {

        log.info("Users received");

        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(long id) {

        log.info("User id received: " + id);
        return userRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь %s не найден.", id)));
    }

    @Override
    public void deleteById(long id) {
        log.info("User {} received to remove", id);

        userRepository.deleteById(id);
    }

}
