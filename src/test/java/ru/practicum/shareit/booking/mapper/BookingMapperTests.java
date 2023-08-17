package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.GetItemBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class BookingMapperTests {

    @Test
    void testMapFromBookingToGetItemBookingDto() {
        User user = User.builder()
                .id(1L)
                .build();

        Booking booking = Booking.builder()
                .id(1L)
                .booker(user)
                .build();

        GetItemBookingDto bookingDto = BookingMapper.mapFromBookingToBookingDto(booking);

        assertThat("Неправильный результат проверки", bookingDto.getId(), equalTo(booking.getId()));
        assertThat("Неправильный результат проверки", bookingDto.getBookerId(), equalTo(booking.getBooker().getId()));
    }

}
