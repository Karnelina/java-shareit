package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.aspect.ToLog;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemAllFieldsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

import static ru.practicum.shareit.util.Constant.*;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@ToLog
@Validated
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto saveItem(@RequestBody ItemDto itemDto,
                            @RequestHeader(REQUEST_HEADER_USER_ID) long userId) {
        Item item = itemService.save(itemDto, userId);
        return ItemMapper.mapToItemDto(item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                              @PathVariable long itemId) {
        Item item = itemService.update(ItemMapper.mapToItem(itemDto), itemId, userId);
        return ItemMapper.mapToItemDto(item);
    }

    @GetMapping("/{itemId}")
    public ItemAllFieldsDto findItemById(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                         @PathVariable long itemId) {

        return itemService.findById(userId, itemId);
    }

    @GetMapping
    public Collection<ItemAllFieldsDto> findItemsByUserId(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                                          @RequestParam(defaultValue = PAGE_DEFAULT_FROM) Short from,
                                                          @RequestParam(defaultValue = PAGE_DEFAULT_SIZE) Short size) {

        Pageable page = PageRequest.of(from / size, size);

        return itemService.findItemsByUserId(userId, page);
    }

    @GetMapping("/search")
    public Collection<ItemAllFieldsDto> searchByText(@RequestParam(name = "text") String text,
                                                     @RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                                     @RequestParam(defaultValue = PAGE_DEFAULT_FROM) Short from,
                                                     @RequestParam(defaultValue = PAGE_DEFAULT_SIZE) Short size) {


        Pageable page = PageRequest.of(from / size, size);

        return itemService.searchByText(text, userId, page);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto saveComment(@PathVariable long itemId, @RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                          @RequestBody CommentRequestDto commentRequestDto) {

        return itemService.saveComment(itemId, userId, commentRequestDto.getText());
    }
}