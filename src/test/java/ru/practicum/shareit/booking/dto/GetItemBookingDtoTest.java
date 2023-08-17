package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class GetItemBookingDtoTest {

    @Autowired
    private JacksonTester<GetItemBookingDto> bookingDtoJacksonTester;

    @Test
    void testBookingDtoJackson() throws IOException {
        GetItemBookingDto bookingDto = new GetItemBookingDto(1L, 1L);

        JsonContent<GetItemBookingDto> jsonContent = bookingDtoJacksonTester.write(bookingDto);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.bookerId")
                .isEqualTo(1);
    }
}
