package ru.practicum.shareit.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemGetOwnItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

@UtilityClass
public class ItemRequestMapper {
    public ItemRequestResponseDto mapToItemRequestResponseDtoWithItemId(ItemRequest itemRequest,
                                                                        Collection<ItemGetOwnItemRequestDto> dtos) {
        return ItemRequestResponseDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(dtos)
                .build();
    }
}
