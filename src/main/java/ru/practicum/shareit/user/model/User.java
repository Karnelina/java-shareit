package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

/**
 * TODO Sprint add-controllers.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Positive
    long id;

    @NotBlank(message = "Введите имя")
    private String name;

    @Email(message = "Неправильно введен email")
    @NotBlank(message = "Введите email")
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
