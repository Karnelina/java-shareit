package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.aspect.ToLog;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.util.Constants.*;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
@ToLog
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public Object saveItemRequest(@Valid @RequestBody ItemRequestRequestDto dto,
                                  @RequestHeader(REQUEST_HEADER_USER_ID) long userId) {
        return itemRequestClient.saveItemRequest(dto, userId);
    }

    @GetMapping
    public Object findOwnItemRequests(@RequestHeader(REQUEST_HEADER_USER_ID) long userId) {
        return itemRequestClient.findOwnItemRequests(userId);
    }

    @GetMapping("/all")
    public Object findAllItemRequests(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                      @RequestParam(defaultValue = PAGE_DEFAULT_FROM) @PositiveOrZero Short from,
                                      @RequestParam(defaultValue = PAGE_DEFAULT_SIZE) @Positive Short size) {
        return itemRequestClient.findAllItemRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public Object findItemRequestById(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                      @PathVariable long requestId) {
        return itemRequestClient.findItemRequestsById(userId, requestId);
    }
}
