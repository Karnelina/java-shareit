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
    private long id;
    @NotEmpty
    private String description;
    @NotEmpty
    private User requestor;
    private LocalDateTime created;

}
