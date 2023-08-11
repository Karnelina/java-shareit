package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserShortDto;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(target = "item", expression = "java(mapToGetBookingItemDto(booking.getItem()))")
    @Mapping(target = "booker", expression = "java(mapToGetBookingUserDto(booking.getBooker()))")
    BookingAllFieldsDto mapToBookingAllFieldsDto(Booking booking);

    @Mapping(source = "booker.id", target = "bookerId")
    GetItemBookingDto mapFromBookingToBookingDto(Booking booking);

    default ItemShortDto mapToGetBookingItemDto(Item item) {
        return new ItemShortDto(item.getId(), item.getName());
    }

    default UserShortDto mapToGetBookingUserDto(User user) {
        return new UserShortDto(user.getId());
    }
}
