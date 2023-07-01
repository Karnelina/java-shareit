package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    Optional<Item> addItem(long userId, ItemDto item);

    Optional<Item> updateItem(Item item);

    List<Item> getAllItemsByUserId(long userId);

    Item getItemById(long id);

    List<Item> findItemByText(String text);

    void removeItem(long id);
}
