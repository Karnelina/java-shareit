package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.validation.StartBeforeEndDateValid;
import ru.practicum.shareit.validation.marker.Create;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static ru.practicum.shareit.util.Constants.TIME_PATTERN;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@StartBeforeEndDateValid
public class CreateBookingDto {
    @NotNull(groups = {Create.class})
    private Long itemId;

    @FutureOrPresent
    @DateTimeFormat(pattern = TIME_PATTERN)
    private LocalDateTime start;

    @DateTimeFormat(pattern = TIME_PATTERN)
    private LocalDateTime end;
}
