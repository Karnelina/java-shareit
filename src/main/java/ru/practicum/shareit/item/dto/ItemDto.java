package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */

@AllArgsConstructor
@Data
public class ItemDto {

    @NotBlank(message = "Введите name")
    String name;

    @NotBlank(message = "Введите description")
    String description;

    @NotNull
    Boolean available;

}
