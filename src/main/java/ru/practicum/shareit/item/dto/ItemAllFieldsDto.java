package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.GetItemBookingDto;

import java.util.Collection;

@Data
@AllArgsConstructor
@Builder
public class ItemAllFieldsDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private long itemRequest;
    private GetItemBookingDto lastBooking;
    private GetItemBookingDto nextBooking;
    Collection<CommentResponseDto> comments;
}
