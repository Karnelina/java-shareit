package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.booking.dto.GetItemBookingDto;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ItemAllFieldsDto extends ItemDto {
    private GetItemBookingDto lastBooking;
    private GetItemBookingDto nextBooking;
    private List<CommentResponseDto> comments;

    public ItemAllFieldsDto(long id, String name, String description, Boolean available,
                            GetItemBookingDto lastBooking, GetItemBookingDto nextBooking, List<CommentResponseDto> comments) {
        super(id, name, description, available);
        this.lastBooking = lastBooking;
        this.nextBooking = nextBooking;
        this.comments = comments;
    }
}
