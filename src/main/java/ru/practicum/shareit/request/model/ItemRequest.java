package ru.practicum.shareit.request.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@NoArgsConstructor
@Data
public class ItemRequest {
    @NotEmpty
    long id;
    @NotEmpty
    String description;
    @NotEmpty
    User requestor;
    LocalDateTime created;

}
