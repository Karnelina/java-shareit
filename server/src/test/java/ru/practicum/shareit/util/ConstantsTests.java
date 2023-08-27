package ru.practicum.shareit.util;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.Booking;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.practicum.shareit.util.Constant.orderByStartDateAsc;
import static ru.practicum.shareit.util.Constant.orderByStartDateDesc;

class ConstantsTests {

    @Test
    void testShouldReturnZeroForEqualStartDatesAsc() {
        Booking booking1 = Booking.builder()
                .start(LocalDateTime.of(2023, 7, 21, 10, 0)).build();

        Booking booking2 = Booking.builder()
                .start(LocalDateTime.of(2023, 7, 21, 10, 0)).build();

        assertEquals(0, orderByStartDateAsc.compare(booking1, booking2), "Неправильный результат сравнения");
    }

    @Test
    void testShouldReturnNegativeForEarlierStartDateAsc() {
        Booking booking1 = Booking.builder()
                .start(LocalDateTime.of(2023, 7, 21, 10, 0)).build();

        Booking booking2 = Booking.builder()
                .start(LocalDateTime.of(2023, 7, 22, 15, 30)).build();

        assertTrue(orderByStartDateAsc.compare(booking1, booking2) < 0, "Неправильный результат сравнения");
    }

    @Test
    void testShouldReturnPositiveForLaterStartDateAsc() {
        Booking booking1 = Booking.builder()
                .start(LocalDateTime.of(2023, 7, 22, 15, 30)).build();

        Booking booking2 = Booking.builder()
                .start(LocalDateTime.of(2023, 7, 21, 10, 0)).build();

        assertTrue(orderByStartDateAsc.compare(booking1, booking2) > 0, "Неправильный результат сравнения");
    }

    @Test
    void testShouldReturnZeroForEqualStartDatesDesc() {
        Booking booking1 = Booking.builder()
                .start(LocalDateTime.of(2023, 7, 21, 10, 0)).build();

        Booking booking2 = Booking.builder()
                .start(LocalDateTime.of(2023, 7, 21, 10, 0)).build();

        assertEquals(0, orderByStartDateDesc.compare(booking1, booking2), "Неправильный результат сравнения");
    }

    @Test
    void testShouldReturnPositiveForEarlierStartDateDesc() {
        Booking booking1 = Booking.builder()
                .start(LocalDateTime.of(2023, 7, 21, 10, 0)).build();

        Booking booking2 = Booking.builder()
                .start(LocalDateTime.of(2023, 7, 22, 15, 30)).build();

        assertTrue(orderByStartDateDesc.compare(booking1, booking2) > 0, "Неправильный результат сравнения");
    }

    @Test
    void testShouldReturnNegativeForLaterStartDateDesc() {
        Booking booking1 = Booking.builder()
                .start(LocalDateTime.of(2023, 7, 22, 15, 30)).build();

        Booking booking2 = Booking.builder()
                .start(LocalDateTime.of(2023, 7, 21, 10, 0)).build();

        assertTrue(orderByStartDateDesc.compare(booking1, booking2) < 0, "Неправильный результат сравнения");
    }

    @Test
    void testShouldReturnZeroForEqualStartAndEndDatesAsc() {
        Booking booking1 = Booking.builder()
                .start(LocalDateTime.of(2023, 7, 21, 10, 0)).build();

        Booking booking2 = Booking.builder()
                .start(LocalDateTime.of(2023, 7, 21, 10, 0)).build();

        assertEquals(0, orderByStartDateAsc.compare(booking1, booking2), "Неправильный результат сравнения");
    }

    @Test
    void testShouldReturnZeroForEqualStartAndEndDatesDesc() {
        Booking booking1 = Booking.builder()
                .start(LocalDateTime.of(2023, 7, 21, 10, 0)).build();

        Booking booking2 = Booking.builder()
                .start(LocalDateTime.of(2023, 7, 21, 10, 0)).build();

        assertEquals(0, orderByStartDateDesc.compare(booking1, booking2), "Неправильный результат сравнения");
    }

    @Test
    void testShouldReturnSameResultForEqualBookingsAsc() {
        Booking booking1 = Booking.builder()
                .start(LocalDateTime.of(2023, 7, 21, 10, 0)).build();

        Booking booking2 = Booking.builder()
                .start(LocalDateTime.of(2023, 7, 21, 10, 0)).build();

        int result1 = orderByStartDateAsc.compare(booking1, booking2);
        int result2 = orderByStartDateAsc.compare(booking2, booking1);

        assertEquals(result1, result2, "Неправильный результат сравнения");
    }

    @Test
    void testShouldReturnSameResultForEqualBookingsDesc() {
        Booking booking1 = Booking.builder()
                .start(LocalDateTime.of(2023, 7, 21, 10, 0)).build();

        Booking booking2 = Booking.builder()
                .start(LocalDateTime.of(2023, 7, 21, 10, 0)).build();

        int result1 = orderByStartDateDesc.compare(booking1, booking2);
        int result2 = orderByStartDateDesc.compare(booking2, booking1);

        assertEquals(result1, result2, "Неправильный результат сравнения");
    }

    @Test
    void testShouldSortListOfBookingsAsc() {
        Booking booking1 = Booking.builder()
                .start(LocalDateTime.of(2023, 7, 21, 10, 0)).build();

        Booking booking2 = Booking.builder()
                .start(LocalDateTime.of(2023, 7, 22, 15, 30)).build();

        Booking booking3 = Booking.builder()
                .start(LocalDateTime.of(2023, 7, 20, 8, 45)).build();


        List<Booking> bookings = Arrays.asList(booking1, booking2, booking3);

        bookings.sort(orderByStartDateAsc);

        List<Booking> expectedOrder = List.of(booking3, booking1, booking2);
        assertEquals(expectedOrder, bookings, "Неправильный результат сравнения");
    }

    @Test
    void testShouldSortListOfBookingsDesc() {
        Booking booking1 = Booking.builder()
                .start(LocalDateTime.of(2023, 7, 21, 10, 0)).build();

        Booking booking2 = Booking.builder()
                .start(LocalDateTime.of(2023, 7, 22, 15, 30)).build();

        Booking booking3 = Booking.builder()
                .start(LocalDateTime.of(2023, 7, 20, 8, 45)).build();


        List<Booking> bookings = Arrays.asList(booking1, booking2, booking3);

        bookings.sort(orderByStartDateDesc);

        List<Booking> expectedOrder = List.of(booking2, booking1, booking3);
        assertEquals(expectedOrder, bookings, "Неправильный результат сравнения");
    }
}
