package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.user.dto.UserShortDto;

import java.time.LocalDateTime;

import static ru.practicum.shareit.util.Constants.TIME_PATTERN;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingAllFieldsDto {
    private long id;

    @DateTimeFormat(pattern = TIME_PATTERN)
    private LocalDateTime start;

    @DateTimeFormat(pattern = TIME_PATTERN)
    private LocalDateTime end;

    private ItemShortDto item;

    private UserShortDto booker;

    private Status status;
}
