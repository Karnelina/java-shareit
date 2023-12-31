package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemShortDtoTest {

    @Autowired
    private JacksonTester<ItemShortDto> getBookingItemDtoJacksonTester;

    @Test
    void testGetBookingItemDto() throws IOException {
        ItemShortDto getBookingItemDto = new ItemShortDto(1L, "name");

        JsonContent<ItemShortDto> jsonContent = getBookingItemDtoJacksonTester.write(getBookingItemDto);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);

        assertThat(jsonContent).extractingJsonPathStringValue("$.name")
                .isEqualTo(getBookingItemDto.getName());
    }
}
