package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.GetItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private ItemStorage itemStorage;
    private UserStorage userStorage;

    @Override
    public Optional<Item> addItem(long userId, ItemDto item) {
        if (userStorage.getUserById(userId).isPresent()) {

            log.info("Item {} add", item);

            return itemStorage.addItem(userId, item);
        }

        throw new UserNotFoundException("User Id does not exist" + userId);
    }

    @Override
    public Item updateItem(long userId, long id, ItemDto item) {
        Item oldItem = itemStorage.getItemById(id);

        if (oldItem.getOwner() != userId) {
            throw new ItemNotFoundException("You cannot change Item");
        }

        if (item.getName() != null && !oldItem.getName().isBlank()) {
            oldItem.setName(item.getName());
        }

        if (item.getDescription() != null && !oldItem.getDescription().isBlank()) {
            oldItem.setDescription(item.getDescription());
        }

        if (item.getAvailable() != null) {
            oldItem.setAvailable(item.getAvailable());
        }

        log.info("Item {} patch", oldItem);

        return oldItem;
    }

    @Override
    public List<GetItemDto> getAllItemsByUserId(long userId) {

        log.info("Items received");

        return itemStorage.getAllItemsByUserId(userId).stream().filter(i -> i.getOwner() == userId)
                .map(ItemMapper::toGetItemDto).collect(Collectors.toList());
    }

    @Override
    public GetItemDto getItemById(long id) {
        Item item = itemStorage.getItemById(id);

        log.info("Item {} received", item);

        return ItemMapper.toGetItemDto(item);
    }

    @Override
    public List<GetItemDto> findItemByText(long userId, String text) {

        log.info("Find item " + text);
        if (text.isBlank()) {

            return Collections.emptyList();
        }

        return itemStorage.findItemByText(text).stream().map(ItemMapper::toGetItemDto).collect(Collectors.toList());
    }

    @Override
    public void removeItem(long id) {

        log.info("Item {} remove", id);

        itemStorage.removeItem(id);
    }


}
