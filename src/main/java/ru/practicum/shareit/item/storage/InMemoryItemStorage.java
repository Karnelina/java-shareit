package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class InMemoryItemStorage implements ItemStorage {
    private final Map<Long, Item> items;
    private long id;

    public InMemoryItemStorage() {
        this.items = new HashMap<>();
        this.id = 1L;
    }

    @Override
    public Optional<Item> addItem(long userId, ItemDto item) {
        Item newItem = new Item(id++, item.getName(), item.getDescription(), item.getAvailable(),
                userId);

        log.info("Item {} add", newItem);

        items.put(newItem.getId(), newItem);

        return Optional.of(newItem);
    }

    @Override
    public Optional<Item> updateItem(Item item) {
        if (!items.containsKey(item.getId())) {
            throw new ItemNotFoundException("Id does not exist");
        }

        items.put(item.getId(), item);
        return Optional.of(item);
    }

    @Override
    public List<Item> getAllItemsByUserId(long userId) {

        log.info("Items received" + items.values());

        return new ArrayList<>(items.values());
    }

    @Override
    public Item getItemById(long id) {
        if (items.containsKey(id)) {

            return items.get(id);
        }

        throw new ItemNotFoundException("Id does not exist");
    }

    @Override
    public List<Item> findItemByText(String text) {
        String lowerCaseText = text.toLowerCase();

        return items.values()
                .stream()
                .filter(t -> (t.getName().toLowerCase().contains(lowerCaseText)
                        || t.getDescription().toLowerCase().contains(lowerCaseText))
                        && t.getAvailable())
                .collect(Collectors.toList());
    }

    @Override
    public void removeItem(long id) {
        items.remove(id);
    }


}
