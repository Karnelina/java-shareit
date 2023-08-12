package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.aspect.ToLog;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.marker.Create;
import ru.practicum.shareit.validation.marker.Update;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
@ToLog
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto saveUser(@Validated(Create.class) @RequestBody UserDto userDto) {
        User user = userService.save(UserMapper.INSTANCE.mapToUser(userDto));
        return UserMapper.INSTANCE.mapToUserDto(user);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@Validated(Update.class) @RequestBody UserDto userDto, @PathVariable Long userId) {
        User user = userService.update(UserMapper.INSTANCE.mapToUser(userDto), userId);
        return UserMapper.INSTANCE.mapToUserDto(user);
    }

    @GetMapping("/{id}")
    public UserDto findUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return UserMapper.INSTANCE.mapToUserDto(user);
    }

    @GetMapping
    public Collection<UserDto> findAllUsers() {
        return userService
                .findAll()
                .stream()
                .map(UserMapper.INSTANCE::mapToUserDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteById(id);
    }
}