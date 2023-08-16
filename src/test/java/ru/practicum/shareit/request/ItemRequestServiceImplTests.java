package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemGetOwnItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTests {
    @Mock
    private ItemRequestRepository mockItemRequestRepository;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private ItemRepository mockItemRepository;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    private ItemRequest itemRequest;

    private User user;

    private static LocalDateTime localDateTime;

    private static Pageable page;

    private Item item;

    @BeforeAll
    static void beforeAll() {
        page = null;
        localDateTime = LocalDateTime.now();
    }

    @BeforeEach
    void init() {
        user = User.builder()
                .id(1L)
                .name("name")
                .email("email@email.ru")
                .build();

        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("description")
                .created(localDateTime)
                .requestor(user)
                .build();

        item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .itemRequest(itemRequest)
                .owner(user)
                .build();
    }

    @Test
    void testShouldThrowExceptionWhenUserNotFoundOnSaveItemRequest() {
        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                itemRequestService.saveItemRequest("description", 0L));
        verify(mockUserRepository, times(1)).findById(any());
    }

    @Test
    void testShouldSaveItemRequest() {
        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));

        when(mockItemRequestRepository.save(Mockito.any(ItemRequest.class)))
                .thenReturn(itemRequest);

        assertThat(itemRequest, equalTo(itemRequestService.saveItemRequest(itemRequest.getDescription(), 1L)));

        verify(mockUserRepository, times(1)).findById(anyLong());
        verify(mockItemRequestRepository, times(1)).save(any());
    }

    @Test
    void testShouldThrowExceptionWhenFindAllItemsNotFoundUser() {
        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemRequestService.findOwnItemRequests(1L));
        verify(mockUserRepository, times(1)).findById(anyLong());
    }

    @Test
    void testShouldFindOwnItemRequests() {
        when(mockUserRepository.findById(1L))
                .thenReturn(Optional.ofNullable(user));

        when(mockItemRequestRepository.findItemRequestsByRequestorId(user.getId()))
                .thenReturn(List.of(itemRequest));

        when(mockItemRepository.findItemByItemRequestIn(any()))
                .thenReturn(List.of(item));

        ItemRequestResponseDto result = ItemRequestResponseDto.builder()
                .id(1L)
                .description("description")
                .created(localDateTime)
                .items(Collections
                        .singletonList(
                                ItemGetOwnItemRequestDto.builder()
                                        .id(1L)
                                        .name("name")
                                        .description("description")
                                        .available(true)
                                        .requestId(1L)
                                        .build()))
                .build();

        assertThat(List.of(result), equalTo(itemRequestService.findOwnItemRequests(user.getId())));
        verify(mockUserRepository, times(1)).findById(anyLong());
        verify(mockItemRequestRepository, times(1)).findItemRequestsByRequestorId(anyLong());
        verify(mockItemRepository, times(1)).findItemByItemRequestIn(any());
    }

    @Test
    void testShouldThrowExceptionWhenFindAllItemRequestsWithNotFoundUser() {
        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemRequestService.findAllItemRequests(0L, page));
        verify(mockUserRepository, times(1)).findById(anyLong());
    }

    @Test
    void testShouldAllItemRequests() {
        when(mockUserRepository.findById(1L))
                .thenReturn(Optional.ofNullable(user));

        when(mockItemRepository.findItemByItemRequestIn(any()))
                .thenReturn(List.of(item));

        ItemRequestResponseDto result = ItemRequestResponseDto.builder()
                .id(1L)
                .description("description")
                .created(localDateTime)
                .items(Collections
                        .singletonList(
                                ItemGetOwnItemRequestDto.builder()
                                        .id(1L)
                                        .name("name")
                                        .description("description")
                                        .available(true)
                                        .requestId(1L)
                                        .build()))
                .build();

        assertThat(List.of(result), equalTo(itemRequestService.findAllItemRequests(user.getId(), page)));
        verify(mockUserRepository, times(1)).findById(anyLong());
        verify(mockItemRepository, times(1)).findItemByItemRequestIn(any());
    }

    @Test
    void testShouldThrowExceptionWhenFindItemRequestsByIdWithNotFoundUser() {
        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemRequestService.findItemRequestsById(0L, 1L));
        verify(mockUserRepository, times(1)).findById(anyLong());
    }

    @Test
    void testShouldThrowExceptionWhenFindItemRequestsWhenItemRequestNotFound() {
        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));

        when(mockItemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemRequestService.findItemRequestsById(1L, 0L)
                , "Ошибка отработала неправильно");
        verify(mockUserRepository, times(1)).findById(anyLong());
        verify(mockItemRequestRepository, times(1)).findById(anyLong());
    }

    @Test
    void testShouldFindItemRequestById() {
        when(mockUserRepository.findById(1L))
                .thenReturn(Optional.ofNullable(user));

        when(mockItemRequestRepository.findById(1L))
                .thenReturn(Optional.ofNullable(itemRequest));

        when(mockItemRepository.findItemByItemRequestIn(any()))
                .thenReturn(List.of(item));

        ItemRequestResponseDto result = ItemRequestResponseDto.builder()
                .id(1L)
                .description("description")
                .created(localDateTime)
                .items(Collections
                        .singletonList(
                                ItemGetOwnItemRequestDto.builder()
                                        .id(1L)
                                        .name("name")
                                        .description("description")
                                        .available(true)
                                        .requestId(1L)
                                        .build()))
                .build();

        assertThat("Неправильный результат сравнения", result, equalTo(itemRequestService.findItemRequestsById(user.getId(), itemRequest.getId())));
        verify(mockUserRepository, times(1)).findById(anyLong());
        verify(mockItemRequestRepository, times(1)).findById(anyLong());
        verify(mockItemRepository, times(1)).findItemByItemRequestIn(any());
    }

}
