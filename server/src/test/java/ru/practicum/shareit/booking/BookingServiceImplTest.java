package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.booking.enums.Status.APPROVED;
import static ru.practicum.shareit.booking.enums.Status.REJECTED;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BookingRepository mockBookingRepository;

    @Mock
    private ItemRepository mockItemRepository;

    @Mock
    private UserRepository mockUserRepository;

    private Item item;
    private User user;
    private Booking booking;
    private static LocalDateTime start;
    private static LocalDateTime end;
    private static Pageable page;

    @BeforeAll
    static void beforeAll() {
        page = null;
        start = LocalDateTime.now();
        end = LocalDateTime.now();
    }

    @BeforeEach
    void init() {
        user = User.builder()
                .id(1L)
                .name("name")
                .email("email@email.ru")
                .build();

        item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .owner(user)
                .build();

        booking = Booking.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(item)
                .booker(user)
                .status(APPROVED)
                .build();
    }

    @Test
    void testShouldThrowExceptionWhenUserNotFoundBookingSave() {
        when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                bookingService.save(1L, start, end, 1L), "Ошибка отработала неправильно");
        verify(mockItemRepository, times(1)).findById(anyLong());
    }

    @Test
    void testShouldThrowExceptionIfOwnerIdEqualsBookerIdInSave() {
        when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));

        long itemId = item.getId();
        long userId = user.getId();
        assertThrows(NotFoundException.class, () -> bookingService.save(itemId, start, end, userId),
                "Ошибка отработала неправильно");
        verify(mockItemRepository, times(1)).findById(anyLong());
    }

    @Test
    void testShouldThrowExceptionWhenItemNotAvailableInSave() {
        when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));


        item.setAvailable(false);
        long itemId = item.getId();
        long userId = 99L;
        assertThrows(ValidationException.class, () -> bookingService.save(itemId, start, end, userId),
                "Ошибка отработала неправильно");
        verify(mockItemRepository, times(1)).findById(anyLong());
    }

    @Test
    void testShouldThrowExceptionWhenBookingTimeDontValidInSave() {
        when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));

        long itemId = item.getId();
        long userId = 99L;
        LocalDateTime min = LocalDateTime.MIN;
        LocalDateTime max = LocalDateTime.MAX;
        LocalDateTime now = LocalDateTime.now();

        assertThrows(ValidationException.class, () -> bookingService.save(itemId, start, min, userId),
                "Ошибка отработала неправильно");
        assertThrows(ValidationException.class, () -> bookingService.save(itemId, start, start, userId),
                "Ошибка отработала неправильно");
        assertThrows(ValidationException.class, () ->
                        bookingService.save(itemId, max, now, userId), "Ошибка отработала неправильно");
        assertThrows(ValidationException.class, () ->
                        bookingService.save(itemId, max, min, userId), "Ошибка отработала неправильно");
        verify(mockItemRepository, times(4)).findById(anyLong());
    }

    @Test
    void testShouldThrowExceptionWhenBookerNotFoundInSave() {
        when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));

        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        long itemId = item.getId();
        long userId = 99L;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime max = LocalDateTime.MAX;

        assertThrows(NotFoundException.class, () ->
                bookingService.save(itemId, now, max, userId), "Ошибка отработала неправильно");
        verify(mockItemRepository, times(1)).findById(anyLong());
        verify(mockUserRepository, times(1)).findById(anyLong());
    }

    @Test
    void testShouldSaveBooking() {
        when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));

        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));

        when(mockBookingRepository.save(any()))
                .thenReturn(booking);

        long itemId = item.getId();
        long userId = 99L;

        assertThat("Неправильный результат", booking, equalTo(bookingService.save(itemId, start, LocalDateTime.MAX, userId)));
        verify(mockItemRepository, times(1)).findById(anyLong());
        verify(mockUserRepository, times(1)).findById(anyLong());
        verify(mockBookingRepository, times(1)).save(any());
    }

    @Test
    void testShouldThrowExceptionWhenUserNotFoundInFindUserById() {
        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        long userId = 99L;
        assertThrows(NotFoundException.class, () -> bookingService.findByUserId(userId, "ALL", page),
                "Ошибка отработала неправильно");
        verify(mockUserRepository, times(1)).findById(anyLong());
    }

    @Test
    void testShouldUpdateBookingStatusToApproved() {
        long bookingId = 1L;
        long userId = 3L;
        user.setId(2L);

        Booking existingBooking = Booking.builder()
                .id(bookingId)
                .start(start)
                .end(end)
                .item(item)
                .booker(user)
                .status(Status.WAITING)
                .build();

        when(mockBookingRepository.findById(bookingId))
                .thenReturn(Optional.of(existingBooking));

        when(mockUserRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(mockBookingRepository.save(any(Booking.class)))
                .thenReturn(booking);

        User owner = User.builder()
                .id(3L)
                .name("name")
                .email("email@email.ru")
                .build();
        existingBooking.getItem().setOwner(owner);
        Booking result = bookingService.updateAvailableStatus(bookingId, true, userId);

        assertThat("Неправильный результат", result, equalTo(booking));
        assertThat("Неправильный результат", result.getStatus(), equalTo(Status.APPROVED));

        verify(mockBookingRepository, times(1)).findById(bookingId);
        verify(mockUserRepository, times(1)).findById(userId);
        verify(mockBookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testShouldUpdateBookingStatusToRejected() {
        long bookingId = 1L;
        long userId = 3L;
        user.setId(2L);

        Booking existingBooking = Booking.builder()
                .id(bookingId)
                .start(start)
                .end(end)
                .item(item)
                .booker(user)
                .status(Status.WAITING)
                .build();

        when(mockBookingRepository.findById(bookingId))
                .thenReturn(Optional.of(existingBooking));

        when(mockUserRepository.findById(userId))
                .thenReturn(Optional.of(user));

        booking.setStatus(REJECTED);
        when(mockBookingRepository.save(existingBooking))
                .thenReturn(booking);

        User owner = User.builder()
                .id(3L)
                .name("name")
                .email("email@email.ru")
                .build();
        existingBooking.getItem().setOwner(owner);
        Booking result = bookingService.updateAvailableStatus(bookingId, false, userId);

        assertThat("Неправильный результат", result, equalTo(booking));
        assertThat("Неправильный результат", result.getStatus(), equalTo(REJECTED));

        verify(mockBookingRepository, times(1)).findById(bookingId);
        verify(mockUserRepository, times(1)).findById(userId);
        verify(mockBookingRepository, times(1)).save(existingBooking);
    }

    @Test
    void testShouldThrowExceptionWhenBookingNotFoundInUpdateAvailableStatus() {
        long bookingId = 1L;
        long userId = 99L;

        when(mockBookingRepository.findById(bookingId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                        bookingService.updateAvailableStatus(bookingId, true, userId),
                "Ошибка отработала неправильно");

        verify(mockBookingRepository, times(1)).findById(bookingId);
        verify(mockUserRepository, never()).findById(anyLong());
        verify(mockBookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void testShouldThrowExceptionWhenUserNotFoundInUpdateAvailableStatus() {
        long bookingId = 1L;
        long userId = 99L;

        Booking existingBooking = Booking.builder()
                .id(bookingId)
                .start(start)
                .end(end)
                .item(item)
                .booker(user)
                .status(Status.WAITING)
                .build();

        when(mockBookingRepository.findById(bookingId))
                .thenReturn(Optional.of(existingBooking));

        when(mockUserRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                        bookingService.updateAvailableStatus(bookingId, true, userId),
                "Ошибка отработала неправильно");

        verify(mockBookingRepository, times(1)).findById(bookingId);
        verify(mockUserRepository, times(1)).findById(userId);
        verify(mockBookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void testShouldThrowExceptionWhenBookingIsNotWaitingInUpdateAvailableStatus() {
        long bookingId = 1L;
        long userId = 99L;

        Booking existingBooking = Booking.builder()
                .id(bookingId)
                .start(start)
                .end(end)
                .item(item)
                .booker(user)
                .status(Status.APPROVED)
                .build();

        when(mockBookingRepository.findById(bookingId))
                .thenReturn(Optional.of(existingBooking));

        when(mockUserRepository.findById(userId))
                .thenReturn(Optional.of(user));

        assertThrows(ValidationException.class, () ->
                        bookingService.updateAvailableStatus(bookingId, true, userId),
                "Ошибка отработала неправильно");

        verify(mockBookingRepository, times(1)).findById(bookingId);
        verify(mockUserRepository, times(1)).findById(userId);
        verify(mockBookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void testShouldFindAllBookingsByUserIdWithAllState() {
        long userId = 1L;
        String stateString = "ALL";
        List<Booking> expectedBookings = Arrays.asList(
                Booking.builder().id(1L).build(),
                Booking.builder().id(2L).build()
        );

        when(mockUserRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(mockBookingRepository.findByBooker(user, page))
                .thenReturn(expectedBookings);

        Collection<Booking> result = bookingService.findByUserId(userId, stateString, page);

        assertThat("Неправильный результат", result, equalTo(expectedBookings));

        verify(mockUserRepository, times(1)).findById(userId);
        verify(mockBookingRepository, times(1)).findByBooker(user, page);
    }

    @Test
    void testShouldFindAllBookingsByUserIdWithCurrentState() {
        long userId = 1L;
        String stateString = "CURRENT";
        List<Booking> expectedBookings = Collections.singletonList(
                Booking.builder().id(1L).build()
        );

        when(mockUserRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(mockBookingRepository.findByBookerCurrent(any(), any(), any()))
                .thenReturn(expectedBookings);

        Collection<Booking> result = bookingService.findByUserId(userId, stateString, page);

        assertThat("Неправильный результат", result, equalTo(expectedBookings));

        verify(mockUserRepository, times(1)).findById(userId);
        verify(mockBookingRepository, times(1)).findByBookerCurrent(any(), any(), any());
    }

    @Test
    void testShouldFindAllBookingsByUserIdWithPastState() {
        long userId = 1L;
        String stateString = "PAST";
        List<Booking> expectedBookings = Collections.singletonList(
                Booking.builder().id(1L).build()
        );

        when(mockUserRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(mockBookingRepository.findByBookerPast(any(), any(), any()))
                .thenReturn(expectedBookings);

        Collection<Booking> result = bookingService.findByUserId(userId, stateString, page);

        assertThat("Неправильный результат", result, equalTo(expectedBookings));

        verify(mockUserRepository, times(1)).findById(userId);
        verify(mockBookingRepository, times(1)).findByBookerPast(any(), any(), any());
    }

    @Test
    void testShouldFindAllBookingsByUserIdWithFutureState() {
        long userId = 1L;
        String stateString = "FUTURE";
        List<Booking> expectedBookings = Collections.singletonList(
                Booking.builder().id(1L).build()
        );

        when(mockUserRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(mockBookingRepository.findByBookerFuture(any(), any(), any()))
                .thenReturn(expectedBookings);

        Collection<Booking> result = bookingService.findByUserId(userId, stateString, page);

        assertThat("Неправильный результат", result, equalTo(expectedBookings));

        verify(mockUserRepository, times(1)).findById(userId);
        verify(mockBookingRepository, times(1)).findByBookerFuture(any(), any(), any());
    }

    @Test
    void testShouldFindAllBookingsByUserIdWithWaitingState() {
        long userId = 1L;
        String stateString = "WAITING";
        List<Booking> expectedBookings = Collections.singletonList(
                Booking.builder().id(1L).build()
        );

        when(mockUserRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(mockBookingRepository.findByBookerAndStatus(user, Status.WAITING, page))
                .thenReturn(expectedBookings);

        Collection<Booking> result = bookingService.findByUserId(userId, stateString, page);

        assertThat("Неправильный результат", result, equalTo(expectedBookings));

        verify(mockUserRepository, times(1)).findById(userId);
        verify(mockBookingRepository, times(1)).findByBookerAndStatus(user, Status.WAITING, page);
    }

    @Test
    void testShouldFindAllBookingsByUserIdWithRejectedState() {
        long userId = 1L;
        String stateString = "REJECTED";
        List<Booking> expectedBookings = Collections.singletonList(
                Booking.builder().id(1L).build()
        );

        when(mockUserRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(mockBookingRepository.findByBookerAndStatus(user, REJECTED, page))
                .thenReturn(expectedBookings);

        Collection<Booking> result = bookingService.findByUserId(userId, stateString, page);

        assertThat("Неправильный результат", result, equalTo(expectedBookings));

        verify(mockUserRepository, times(1)).findById(userId);
        verify(mockBookingRepository, times(1)).findByBookerAndStatus(user, REJECTED, page);
    }

    @Test
    void testShouldThrowExceptionWhenUserNotFoundInFindAllBookingsByUserId() {
        long userId = 99L;
        String stateString = "ALL";

        when(mockUserRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                        bookingService.findByUserId(userId, stateString, page),
                "Ошибка отработала неправильно");

        verify(mockUserRepository, times(1)).findById(userId);
        verify(mockBookingRepository, never()).findByBooker(any(User.class), any(Pageable.class));
    }

    @Test
    void testShouldFindAllBookingsByOwnerWithAllState() {
        long userId = 1L;
        String stateString = "ALL";
        List<Booking> expectedBookings = Arrays.asList(
                Booking.builder().id(1L).build(),
                Booking.builder().id(2L).build()
        );

        when(mockUserRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(mockBookingRepository.findByItemOwner(user, page))
                .thenReturn(expectedBookings);

        Collection<Booking> result = bookingService.findOwnerBookings(userId, stateString, page);

        assertThat("Неправильный результат", result, equalTo(expectedBookings));

        verify(mockUserRepository, times(1)).findById(userId);
        verify(mockBookingRepository, times(1)).findByItemOwner(user, page);
    }

    @Test
    void testShouldFindAllBookingsByOwnerWithCurrentState() {
        long userId = 1L;
        String stateString = "CURRENT";
        List<Booking> expectedBookings = Collections.singletonList(
                Booking.builder().id(1L).build()
        );

        when(mockUserRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(mockBookingRepository.findByItemOwnerCurrent(any(), any(), any()))
                .thenReturn(expectedBookings);

        Collection<Booking> result = bookingService.findOwnerBookings(userId, stateString, page);

        assertThat(result, equalTo(expectedBookings));

        verify(mockUserRepository, times(1)).findById(userId);
        verify(mockBookingRepository, times(1)).findByItemOwnerCurrent(any(), any(), any());
    }

    @Test
    void testShouldFindAllBookingsByOwnerWithPastState() {
        long userId = 1L;
        String stateString = "PAST";
        List<Booking> expectedBookings = Collections.singletonList(
                Booking.builder().id(1L).build()
        );

        when(mockUserRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(mockBookingRepository.findByItemOwnerPast(any(), any(), any()))
                .thenReturn(expectedBookings);

        Collection<Booking> result = bookingService.findOwnerBookings(userId, stateString, page);

        assertThat("Неправильный результат", result, equalTo(expectedBookings));

        verify(mockUserRepository, times(1)).findById(userId);
        verify(mockBookingRepository, times(1)).findByItemOwnerPast(any(), any(), any());
    }

    @Test
    void testShouldFindAllBookingsByOwnerWithFutureState() {
        long userId = 1L;
        String stateString = "FUTURE";
        List<Booking> expectedBookings = Collections.singletonList(
                Booking.builder().id(1L).build()
        );

        when(mockUserRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(mockBookingRepository.findByItemOwnerFuture(any(), any(), any()))
                .thenReturn(expectedBookings);

        Collection<Booking> result = bookingService.findOwnerBookings(userId, stateString, page);

        assertThat("Неправильный результат", result, equalTo(expectedBookings));

        verify(mockUserRepository, times(1)).findById(userId);
        verify(mockBookingRepository, times(1)).findByItemOwnerFuture(any(), any(), any());
    }

    @Test
    void testShouldFindAllBookingsByOwnerWithWaitingState() {
        long userId = 1L;
        String stateString = "WAITING";
        List<Booking> expectedBookings = Collections.singletonList(
                Booking.builder().id(1L).build()
        );

        when(mockUserRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(mockBookingRepository.findByItemOwnerAndStatus(user, Status.WAITING, page))
                .thenReturn(expectedBookings);

        Collection<Booking> result = bookingService.findOwnerBookings(userId, stateString, page);

        assertThat("Неправильный результат", result, equalTo(expectedBookings));

        verify(mockUserRepository, times(1)).findById(userId);
        verify(mockBookingRepository, times(1)).findByItemOwnerAndStatus(user, Status.WAITING, page);
    }

    @Test
    void testShouldFindAllBookingsByOwnerWithRejectedState() {
        long userId = 1L;
        String stateString = "REJECTED";
        List<Booking> expectedBookings = Collections.singletonList(
                Booking.builder().id(1L).build()
        );

        when(mockUserRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(mockBookingRepository.findByItemOwnerAndStatus(user, REJECTED, page))
                .thenReturn(expectedBookings);

        Collection<Booking> result = bookingService.findOwnerBookings(userId, stateString, page);

        assertThat("Неправильный результат", result, equalTo(expectedBookings));

        verify(mockUserRepository, times(1)).findById(userId);
        verify(mockBookingRepository, times(1)).findByItemOwnerAndStatus(user, REJECTED, page);
    }

    @Test
    void testShouldThrowExceptionWhenUserNotFoundInFindAllBookingsByOwner() {
        long userId = 99L;
        String stateString = "ALL";

        when(mockUserRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                bookingService.findOwnerBookings(userId, stateString, page), "Ошибка отработала неправильно");

        verify(mockUserRepository, times(1)).findById(userId);
        verify(mockBookingRepository, never()).findByItemOwner(any(User.class), any(Pageable.class));
    }

    @Test
    void testShouldFindBookingByBookingIdAndUserIdWhenUserIsBooker() {
        long bookingId = 1L;
        long userId = 99L;

        Booking booking = Booking.builder()
                .id(bookingId)
                .booker(User.builder().id(userId).build())
                .item(Item.builder().owner(User.builder().id(123L).build()).build())
                .build();

        User user = User.builder().id(userId).build();

        when(mockBookingRepository.findById(bookingId))
                .thenReturn(Optional.of(booking));

        when(mockUserRepository.findById(userId))
                .thenReturn(Optional.of(user));

        Booking result = bookingService.findAllBookingsByUserId(bookingId, userId);
        assertThat("Неправильный результат", result, equalTo(booking));

        verify(mockBookingRepository, times(1)).findById(bookingId);
        verify(mockUserRepository, times(1)).findById(userId);
    }

    @Test
    void testShouldFindBookingByBookingIdAndUserIdWhenUserIsOwner() {
        long bookingId = 1L;
        long userId = 123L;

        Booking booking = Booking.builder()
                .id(bookingId)
                .booker(User.builder().id(99L).build())
                .item(Item.builder().owner(User.builder().id(userId).build()).build())
                .build();

        User user = User.builder().id(userId).build();

        when(mockBookingRepository.findById(bookingId))
                .thenReturn(Optional.of(booking));

        when(mockUserRepository.findById(userId))
                .thenReturn(Optional.of(user));

        Booking result = bookingService.findAllBookingsByUserId(bookingId, userId);
        assertThat("Неправильный результат", result, equalTo(booking));

        verify(mockBookingRepository, times(1)).findById(bookingId);
        verify(mockUserRepository, times(1)).findById(userId);
    }


    @Test
    void testShouldThrowNotFoundExceptionWhenBookingNotFound() {
        long bookingId = 1L;
        long userId = 99L;

        when(mockBookingRepository.findById(bookingId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookingService.findAllBookingsByUserId(bookingId, userId),
                "Ошибка отработала неправильно");

        verify(mockBookingRepository, times(1)).findById(bookingId);
        verify(mockUserRepository, never()).findById(anyLong());
    }

    @Test
    void testShouldThrowNotFoundExceptionWhenUserNotFound() {
        long bookingId = 1L;
        long userId = 99L;

        Booking booking = Booking.builder()
                .id(bookingId)
                .booker(User.builder().id(123L).build())
                .item(Item.builder().owner(User.builder().id(123L).build()).build())
                .build();

        when(mockBookingRepository.findById(bookingId))
                .thenReturn(Optional.of(booking));

        when(mockUserRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookingService.findAllBookingsByUserId(bookingId, userId),
                "Ошибка отработала неправильно");

        verify(mockBookingRepository, times(1)).findById(bookingId);
        verify(mockUserRepository, times(1)).findById(userId);
    }

    @Test
    void testShouldThrowNotFoundExceptionWhenNoAvailableBookingsForUser() {
        when(mockBookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        when(mockUserRepository.findById(any()))
                .thenReturn(Optional.ofNullable(user));

        long bookingId = booking.getId();

        assertThrows(NotFoundException.class, () -> bookingService.updateAvailableStatus(
                bookingId, true, bookingId), "Ошибка отработала неправильно");
    }
}

