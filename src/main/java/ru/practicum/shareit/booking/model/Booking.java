package ru.practicum.shareit.booking.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@NoArgsConstructor
@Data
public class Booking {
    @NotEmpty
    long id;
    LocalDateTime start;
    LocalDateTime end;
    @NotEmpty
    Item item;
    @NotEmpty
    User booker;
    Status status;

}
