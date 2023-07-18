package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemAllFieldsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import java.util.Collection;

import static ru.practicum.shareit.util.Constants.REQUEST_HEADER_USER_ID;

/**
 * TODO Sprint add-controllers.
 */
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto saveItem(@Validated(Create.class) @RequestBody ItemDto itemDto,
                            @RequestHeader(REQUEST_HEADER_USER_ID) long userId) {
        Item item = itemService.save(ItemMapper.mapToItem(itemDto), userId);

        log.info("Post saveItem received");

        return ItemMapper.mapToItemDto(item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@Validated(Update.class) @RequestBody ItemDto itemDto,
                              @RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                              @PathVariable long itemId) {
        Item item = itemService.update(ItemMapper.mapToItem(itemDto), itemId, userId);

        log.info("Patch updateItem received");

        return ItemMapper.mapToItemDto(item);
    }

    @GetMapping("/{itemId}")
    public ItemDto findItemById(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                @PathVariable long itemId) {

        log.info("Get findItemById received");

        return itemService.findById(userId, itemId);
    }

    @GetMapping
    public Collection<ItemAllFieldsDto> findItemsByUserId(@RequestHeader(REQUEST_HEADER_USER_ID) long userId) {

        log.info("Get findItemsByUserId received");

        return itemService.findItemsByUserId(userId);
    }

    @GetMapping("/search")
    public Collection<ItemAllFieldsDto> searchByText(@RequestParam(name = "text") String text,
                                                     @RequestHeader(REQUEST_HEADER_USER_ID) long userId) {

        log.info("Get searchByText received");

        return itemService.searchByText(text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto saveComment(@PathVariable long itemId, @RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                          @RequestBody CommentRequestDto commentRequestDto) {

        log.info("Get saveComment received");

        return itemService.saveComment(itemId, userId, commentRequestDto.getText());
    }
}