package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.aspect.ToLog;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.validation.marker.Create;
import ru.practicum.shareit.validation.marker.Update;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collections;

import static ru.practicum.shareit.util.Constants.*;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Validated
@ToLog
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public Object saveItem(@Validated(Create.class) @RequestBody ItemDto itemDto,
                           @RequestHeader(REQUEST_HEADER_USER_ID) long userId) {
        return itemClient.saveItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public Object updateItem(@Validated(Update.class) @RequestBody ItemDto itemDto,
                             @RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                             @PathVariable long itemId) {
        return itemClient.updateItem(itemDto, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public Object findItemById(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                               @PathVariable long itemId) {
        return itemClient.findItemById(userId, itemId);
    }

    @GetMapping
    public Object findItemsByUserId(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                    @RequestParam(defaultValue = PAGE_DEFAULT_FROM) @PositiveOrZero Short from,
                                    @RequestParam(defaultValue = PAGE_DEFAULT_SIZE) @Positive Short size) {
        return itemClient.findItemsByUserId(userId, from, size);
    }

    @GetMapping("/search")
    public Object searchByText(@RequestParam(name = "text") String text,
                               @RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                               @RequestParam(defaultValue = PAGE_DEFAULT_FROM) @PositiveOrZero Short from,
                               @RequestParam(defaultValue = PAGE_DEFAULT_SIZE) @Positive Short size) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemClient.searchByText(text, userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public Object saveComment(@PathVariable long itemId, @RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                              @RequestBody CommentRequestDto commentRequestDto) {
        return itemClient.saveComment(itemId, userId, commentRequestDto);
    }
}
