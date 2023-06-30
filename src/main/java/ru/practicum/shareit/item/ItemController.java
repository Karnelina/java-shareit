package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.GetItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.util.Constants.REQUEST_HEADER_USER_ID;

/**
 * TODO Sprint add-controllers.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping()
    public Optional<Item> addItem(@RequestHeader(REQUEST_HEADER_USER_ID) long userId, @Valid @RequestBody ItemDto item) {

        return itemService.addItem(userId, item);
    }

    @PatchMapping("/{id}")
    public Item updateItem(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                           @PathVariable long id, @RequestBody ItemDto item) {

        return itemService.updateItem(userId, id, item);
    }

    @GetMapping()
    public List<GetItemDto> getAllItemsByUserId(@RequestHeader(REQUEST_HEADER_USER_ID) long userId) {
        return itemService.getAllItemsByUserId(userId);
    }

    @GetMapping("/{id}")
    public GetItemDto getItemById(@PathVariable long id) {
        return itemService.getItemById(id);
    }

    @GetMapping(value = "/search", params = "text")
    public List<GetItemDto> findItemByText(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                           @RequestParam(defaultValue = "") String text) {

        return itemService.findItemByText(userId, text);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        itemService.removeItem(id);
    }
}