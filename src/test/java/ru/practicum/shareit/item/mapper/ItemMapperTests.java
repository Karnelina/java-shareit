package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class ItemMapperTests {
    @Test
    void testMapToItemDto() {
        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .itemRequest(new ItemRequest())
                .build();

        ItemDto itemDto = ItemMapper.mapToItemDto(item);

        assertThat("Неправильный результат id", item.getId(), equalTo(itemDto.getId()));
        assertThat("Неправильный результат название", item.getName(), equalTo(itemDto.getName()));
        assertThat("Неправильный результат описание", item.getDescription(), equalTo(itemDto.getDescription()));
        assertThat("Неправильный результат доступа", item.getAvailable(), equalTo(itemDto.getAvailable()));
        assertThat("Неправильный результат запроса", item.getItemRequest().getId(), equalTo(itemDto.getRequestId()));
    }
}
