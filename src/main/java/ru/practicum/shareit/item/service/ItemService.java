package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.GetItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    Optional<Item> addItem(long userId, ItemDto item);

    Item updateItem(long userId, long id, ItemDto item);

    List<GetItemDto> getAllItemsByUserId(long userId);

    GetItemDto getItemById(long id);

    List<GetItemDto> findItemByText(long userId, String text);

    void removeItem(long id);
}
