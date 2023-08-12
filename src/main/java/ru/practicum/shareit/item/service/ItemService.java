package ru.practicum.shareit.item.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemAllFieldsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {
    Item save(ItemDto  item, Long userId);

    Item update(Item item, Long itemId, Long userId);

    ItemAllFieldsDto findById(Long userId, Long itemId);

    Collection<ItemAllFieldsDto> searchByText(String text, Long userId, Pageable page);

    Collection<ItemAllFieldsDto> findItemsByUserId(Long userId, Pageable page);

    CommentResponseDto saveComment(Long itemId, Long userId, String text);
}
