package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequest saveItemRequest(String description, Long userId);

    Collection<ItemRequestResponseDto> findOwnItemRequests(Long userId);

    Collection<ItemRequestResponseDto> findAllItemRequests(Long userId, Pageable page);

    ItemRequestResponseDto findItemRequestsById(Long userId, Long requestId);
}
