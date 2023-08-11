package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.aspect.ToLog;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.util.OffsetBasedPageRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

import static ru.practicum.shareit.util.Constants.*;

@RestController
@RequestMapping(path = "/requests")
@ToLog
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequest saveItemRequest(@Valid @RequestBody ItemRequestRequestDto dto,
                                       @RequestHeader(REQUEST_HEADER_USER_ID) long userId) {
        return itemRequestService.saveItemRequest(dto.getDescription(), userId);
    }

    @GetMapping
    public Collection<ItemRequestResponseDto> findOwnItemRequests(@RequestHeader(REQUEST_HEADER_USER_ID) long userId) {
        return itemRequestService.findOwnItemRequests(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestResponseDto> findAllItemRequests(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                                                  @RequestParam(defaultValue = PAGE_DEFAULT_FROM) @PositiveOrZero Short from,
                                                                  @RequestParam(defaultValue = PAGE_DEFAULT_SIZE) @Positive Short size) {

        Pageable page = new OffsetBasedPageRequest(from, size, Sort.by("created").descending());
        return itemRequestService.findAllItemRequests(userId, page);
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto findItemRequestById(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                                      @PathVariable long requestId) {
        return itemRequestService.findItemRequestsById(userId, requestId);
    }
}
