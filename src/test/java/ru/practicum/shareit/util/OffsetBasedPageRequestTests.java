package ru.practicum.shareit.util;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class OffsetBasedPageRequestTests {

    @Test
    void testOffsetBasedPageRequestWithValidOffsetAndLimit() {
        int offset = 10;
        int limit = 20;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        OffsetBasedPageRequest pageRequest = new OffsetBasedPageRequest(offset, limit, sort);

        assertEquals(offset, pageRequest.getOffset(), "Неправильный результат сравнения");
        assertEquals(limit, pageRequest.getPageSize(), "Неправильный результат сравнения");
        assertEquals(sort, pageRequest.getSort(), "Неправильный результат сравнения");
    }

    @Test
    void testOffsetBasedPageRequestWithNegativeOffsetShouldThrowException() {
        int offset = -10;
        int limit = 20;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");

        assertThrows(IllegalArgumentException.class, () -> new OffsetBasedPageRequest(offset, limit, sort),
                "Ошибка отработала неправильно");
    }

    @Test
    void testOffsetBasedPageRequestWithLimitLessThanOneShouldThrowException() {
        int offset = 10;
        int limit = 0;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");

        assertThrows(IllegalArgumentException.class, () -> new OffsetBasedPageRequest(offset, limit, sort),
                "Ошибка отработала неправильно");
    }

    @Test
    void testOffsetBasedPageRequestWithValidOffsetAndLimitAndSortDirection() {
        int offset = 5;
        int limit = 15;
        OffsetBasedPageRequest pageRequest = new OffsetBasedPageRequest(offset, limit, Sort.Direction.DESC, "name");

        assertEquals(offset, pageRequest.getOffset(), "Неправильный результат сравнения");
        assertEquals(limit, pageRequest.getPageSize(), "Неправильный результат сравнения");
        assertTrue(pageRequest.getSort().isSorted(), "Неправильный результат сравнения");
        assertEquals(Sort.Direction.DESC, Objects.requireNonNull(pageRequest.getSort()
                .getOrderFor("name")).getDirection(), "Неправильный результат сравнения");
    }

    @Test
    void testOffsetBasedPageRequestWithValidOffsetAndLimitAndUnsorted() {
        int offset = 0;
        int limit = 25;
        OffsetBasedPageRequest pageRequest = new OffsetBasedPageRequest(offset, limit);

        assertEquals(offset, pageRequest.getOffset(), "Неправильный результат сравнения");
        assertEquals(limit, pageRequest.getPageSize(), "Неправильный результат сравнения");
        assertFalse(pageRequest.getSort().isSorted(), "Неправильный результат сравнения");
    }

    @Test
    void testGetPageNumber() {
        int offset = 25;
        int limit = 10;
        OffsetBasedPageRequest pageRequest = new OffsetBasedPageRequest(offset, limit);

        assertEquals(2, pageRequest.getPageNumber(), "Неправильный результат сравнения");
    }

    @Test
    void testNext() {
        int offset = 10;
        int limit = 5;
        OffsetBasedPageRequest pageRequest = new OffsetBasedPageRequest(offset, limit);

        Pageable next = pageRequest.next();
        assertEquals(offset + limit, next.getOffset(), "Неправильный результат сравнения");
        assertEquals(limit, next.getPageSize(), "Неправильный результат сравнения");
    }

    @Test
    void testPrevious() {
        int offset = 15;
        int limit = 5;
        OffsetBasedPageRequest pageRequest = new OffsetBasedPageRequest(offset, limit);

        Pageable previous = pageRequest.previous();
        assertEquals(offset - limit, previous.getOffset(), "Неправильный результат сравнения");
        assertEquals(limit, previous.getPageSize(), "Неправильный результат сравнения");
    }

    @Test
    void testPreviousOrFirst() {
        int offset = 10;
        int limit = 5;
        OffsetBasedPageRequest pageRequest = new OffsetBasedPageRequest(offset, limit);

        Pageable previousOrFirst = pageRequest.previousOrFirst();
        assertEquals(offset - limit, previousOrFirst.getOffset(), "Неправильный результат сравнения");
        assertEquals(limit, previousOrFirst.getPageSize(), "Неправильный результат сравнения");

        pageRequest = new OffsetBasedPageRequest(offset - limit, limit);
        previousOrFirst = pageRequest.previousOrFirst();
        assertEquals(0, previousOrFirst.getOffset(), "Неправильный результат сравнения");
        assertEquals(limit, previousOrFirst.getPageSize(), "Неправильный результат сравнения");
    }

    @Test
    void testFirst() {
        int offset = 10;
        int limit = 5;
        OffsetBasedPageRequest pageRequest = new OffsetBasedPageRequest(offset, limit);

        Pageable first = pageRequest.first();
        assertEquals(0, first.getOffset(), "Неправильный результат сравнения");
        assertEquals(limit, first.getPageSize(), "Неправильный результат сравнения");
    }

    @Test
    void testHasPrevious() {
        int offset = 10;
        int limit = 5;
        OffsetBasedPageRequest pageRequest = new OffsetBasedPageRequest(offset, limit);

        assertTrue(pageRequest.hasPrevious(), "Неправильный результат сравнения");

        pageRequest = new OffsetBasedPageRequest(offset - limit, limit);
        assertFalse(pageRequest.hasPrevious(), "Неправильный результат сравнения");
    }

    @Test
    void testEqualsAndHashCode() {
        int offset1 = 10;
        int limit1 = 5;
        Sort sort1 = Sort.by(Sort.Direction.ASC, "id");
        OffsetBasedPageRequest pageRequest1 = new OffsetBasedPageRequest(offset1, limit1, sort1);

        int offset2 = 10;
        int limit2 = 5;
        Sort sort2 = Sort.by(Sort.Direction.ASC, "id");
        OffsetBasedPageRequest pageRequest2 = new OffsetBasedPageRequest(offset2, limit2, sort2);

        assertEquals(pageRequest1, pageRequest2, "Неправильный результат сравнения");
        assertEquals(pageRequest1.hashCode(), pageRequest2.hashCode(), "Неправильный результат сравнения");

        pageRequest2 = new OffsetBasedPageRequest(offset2, 3, sort2);

        assertNotEquals(pageRequest1, pageRequest2, "Неправильный результат сравнения");
        assertNotEquals(pageRequest1.hashCode(), pageRequest2.hashCode(), "Неправильный результат сравнения");
    }
}
