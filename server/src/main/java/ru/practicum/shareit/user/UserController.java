package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.aspect.ToLog;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@ToLog
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto saveUser(@RequestBody UserDto userDto) {
        User user = userService.save(UserMapper.mapToUser(userDto));
        return UserMapper.mapToUserDto(user);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable long userId) {
        User user = userService.update(UserMapper.mapToUser(userDto), userId);
        return UserMapper.mapToUserDto(user);
    }

    @GetMapping("/{id}")
    public UserDto findUserById(@PathVariable long id) {
        User user = userService.findById(id);
        return UserMapper.mapToUserDto(user);
    }

    @GetMapping
    public Collection<UserDto> findAllUsers() {
        return userService
                .findAll()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable long id) {
        userService.deleteById(id);
    }
}