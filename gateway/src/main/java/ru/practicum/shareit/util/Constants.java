package ru.practicum.shareit.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;

@UtilityClass
public class Constants {

    public static final String REQUEST_HEADER_USER_ID = "X-Sharer-User-Id";
    public static final Sort SORT_BY_START_DATE_DESC = Sort.by(Sort.Direction.DESC, "start");
    public static final String ERROR_RESPONSE = "error";
    public static final String TIME_PATTERN = "YYYY-MM-DDTHH:mm:ss";
    public static final String PAGE_DEFAULT_FROM = "0";
    public static final String PAGE_DEFAULT_SIZE = "32";

}
