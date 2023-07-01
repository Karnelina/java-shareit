package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Data
public class UserDto {

    @NotBlank(message = "Введите имя")
    private String name;

    @Email(message = "Неправильно введен email")
    @NotBlank(message = "Введите email")
    private String email;
}