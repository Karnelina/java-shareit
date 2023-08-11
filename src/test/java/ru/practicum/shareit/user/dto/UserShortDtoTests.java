package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserShortDtoTests {

    @Autowired
    private JacksonTester<UserShortDto> getBookingUserDtoJacksonTester;

    @Test
    void getBookingUserDtoTest() throws IOException {
        UserShortDto getBookingUserDto = new UserShortDto(1L);

        JsonContent<UserShortDto> jsonContent = getBookingUserDtoJacksonTester.write(getBookingUserDto);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
    }
}
