package ru.practicum.shareit.booking;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingAllFieldsDto;
import ru.practicum.shareit.booking.dto.GetItemBookingDto;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.user.dto.UserShortDto;

@UtilityClass
public class BookingMapper {
    public BookingAllFieldsDto mapToBookingAllFieldsDto(Booking booking) {
        return BookingAllFieldsDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(new ItemShortDto(booking.getItem().getId(), booking.getItem().getName()))
                .booker(new UserShortDto(booking.getBooker().getId()))
                .status(booking.getStatus())
                .build();
    }

    public GetItemBookingDto mapFromBookingToBookingDto(Booking booking) {
        return GetItemBookingDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }
}
