package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping()
    public UserDto saveUser(@Validated(Create.class) @RequestBody UserDto userDto) {

        User user = userService.save(UserMapper.mapToUser(userDto));

        log.info("Post saveUser received");

        return UserMapper.mapToUserDto(user);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@Validated(Update.class) @PathVariable long id, @RequestBody UserDto userDto) {
        User user = userService.update(id, UserMapper.mapToUser(userDto));

        log.info("Patch updateUser received");

        return UserMapper.mapToUserDto(user);
    }

    @GetMapping()
    public List<UserDto> findAllUsers() {

        log.info("Get findAllUsers received");

        return userService
                .findAll()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable long id) {

        log.info("Get findUserById received");

        return userService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable long id) {

        log.info("Delete deleteUserById received");

        userService.deleteById(id);
    }

}
