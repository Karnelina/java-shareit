package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.user.dto.UserShortDto;

@UtilityClass
public class BookingMapper {
    public BookingAllFieldsDto mapToAllFieldsBooking(Booking booking) {
        return BookingAllFieldsDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(new ItemShortDto(booking.getItem().getId(), booking.getItem().getName()))
                .booker(new UserShortDto(booking.getBooker().getId()))
                .status(booking.getStatus())
                .build();
    }

    public GetItemBookingDto toGetItemBookingDtoFromBooking(Booking booking) {
        if (booking == null) {
            return null;
        }

        return GetItemBookingDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }
}
