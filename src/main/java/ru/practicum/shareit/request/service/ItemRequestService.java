package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequest saveItemRequest(String description, long userId);

    Collection<ItemRequestResponseDto> findOwnItemRequests(long userId);

    Collection<ItemRequestResponseDto> findAllItemRequests(long userId, Pageable page);

    ItemRequestResponseDto findItemRequestsById(long userId, long requestId);
}
