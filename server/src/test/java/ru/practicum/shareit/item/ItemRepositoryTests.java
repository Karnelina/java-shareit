package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ItemRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRepository itemRepository;

    private static Pageable page = PageRequest.of(0, 5);

    User user = User.builder()
            .id(null)
            .name("Smith")
            .email("smith@mail.com")
            .build();
    Item item1;
    Item item2;
    Item item3;

    @BeforeEach
    void beforeEach() {
        entityManager.persist(user);
        item1 = Item.builder()
                .id(null)
                .name("pen1")
                .description("black pen")
                .available(true)
                .owner(user)
                .build();
        item2 = Item.builder()
                .id(null)
                .name("pen2")
                .description("sharp pen")
                .available(true)
                .owner(user)
                .build();
        item3 = Item.builder()
                .id(null)
                .name("pen3")
                .description("black pen")
                .available(true)
                .owner(user)
                .build();
        entityManager.persist(item1);
        entityManager.persist(item2);
        entityManager.persist(item3);
    }

    @Test
    void testShouldReturnAllItems() {
        List<Item> items = itemRepository.findItemsByText("pen", page);
        assertTrue(items.contains(item1), "Неправильный результат проверки");
        assertTrue(items.contains(item2), "Неправильный результат проверки");
        assertTrue(items.contains(item3), "Неправильный результат проверки");

        assertThat("Неправильный результат размера", items, hasSize(3));
    }

    @Test
    void testShouldReturnTwoItems() {
        List<Item> items = itemRepository.findItemsByText("black pen", page);
        assertThat("Неправильный результат", items, containsInAnyOrder(item1, item3));
        assertThat("Неправильный результат", items, hasSize(2));
    }

    @Test
    void testShouldReturnNoItems() {
        List<Item> items = itemRepository.findItemsByText("brick", page);
        assertThat("Неправильный результат", items.size(), equalTo(0));
    }
}
