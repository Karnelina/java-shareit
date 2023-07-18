package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingAllFieldsDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.validation.ValuesAllowedConstraint;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

import static ru.practicum.shareit.util.Constants.REQUEST_HEADER_USER_ID;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingAllFieldsDto saveBooking(@Valid @RequestBody CreateBookingDto createBookingDto,
                                           @RequestHeader(REQUEST_HEADER_USER_ID) long userId) {
        Booking booking = bookingService.save(
                createBookingDto.getItemId(),
                createBookingDto.getStart(),
                createBookingDto.getEnd(),
                userId);

        log.info("Post saveBooking received" + booking);
        return BookingMapper.mapToAllFieldsBooking(booking);
    }

    @GetMapping
    public Collection<BookingAllFieldsDto> findAllBookingsByUserId(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                                                   @ValuesAllowedConstraint(propName = "state",
                                                                           values = {"all",
                                                                                   "current",
                                                                                   "past",
                                                                                   "future",
                                                                                   "waiting",
                                                                                   "rejected"},
                                                                           message = "Unknown state: UNSUPPORTED_STATUS")
                                                                   @RequestParam(defaultValue = "all") String state) {
        log.info("Get findAllBookingsByUserId received");

        return bookingService.findByUserId(userId, state)
                .stream()
                .map(BookingMapper::mapToAllFieldsBooking)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{bookingId}")
    public BookingAllFieldsDto updateAvailableStatus(@PathVariable long bookingId,
                                                     @RequestParam(required = false) Boolean approved,
                                                     @RequestHeader(REQUEST_HEADER_USER_ID) long userId) {

        Booking booking = bookingService.updateAvailableStatus(bookingId, approved, userId);

        log.info("Patch updateAvailableStatus received");

        return BookingMapper.mapToAllFieldsBooking(booking);
    }

    @GetMapping("/{bookingId}")
    public BookingAllFieldsDto findBookingByUserOwner(@PathVariable long bookingId,
                                                      @RequestHeader(value = REQUEST_HEADER_USER_ID) long userId) {

        Booking booking = bookingService.findAllBookingsByUserId(bookingId, userId);

        log.info("Get findBookingByUserOwner received");

        return BookingMapper.mapToAllFieldsBooking(booking);
    }

    @GetMapping("/owner")
    public Collection<BookingAllFieldsDto> findOwnerBookings(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                                             @ValuesAllowedConstraint(propName = "state",
                                                                     values = {"all",
                                                                             "current",
                                                                             "past",
                                                                             "future",
                                                                             "waiting",
                                                                             "rejected"},
                                                                     message = "Unknown state: UNSUPPORTED_STATUS")
                                                             @RequestParam(defaultValue = "all") String state) {

        log.info("Get findOwnerBookings received");

        return bookingService.findOwnerBookings(userId, state)
                .stream()
                .map(BookingMapper::mapToAllFieldsBooking)
                .collect(Collectors.toList());
    }
}
