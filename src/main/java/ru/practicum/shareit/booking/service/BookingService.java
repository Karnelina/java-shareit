package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.Collection;

public interface BookingService {
    Booking save(Long itemId, LocalDateTime start, LocalDateTime end, Long userId);

    Collection<Booking> findByUserId(Long userId, String state, Pageable page);

    Booking updateAvailableStatus(Long bookingId, Boolean state, Long userId);

    Booking findAllBookingsByUserId(Long bookingId, Long userId);

    Collection<Booking> findOwnerBookings(Long userId, String state, Pageable page);
}
