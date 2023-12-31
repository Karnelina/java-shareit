package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemAllFieldsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static java.time.LocalDateTime.MAX;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemIntegrationTests {
    private final UserService userService;
    private final EntityManager entityManager;
    private final ItemService itemService;
    private final BookingService bookingService;
    private static User user;
    private static User secondUser;
    private Item item;
    private ItemDto itemDto;
    private static Pageable page;

    @BeforeAll
    static void beforeAll() {
        page = PageRequest.of(0, 5);
    }

    @BeforeEach
    void init() {
        itemDto = ItemDto.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();

        item = Item.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();

        user = User.builder()
                .name("name")
                .email("email@email.ru")
                .build();

        secondUser = User.builder()
                .name("second")
                .email("second@email.ru")
                .build();
    }

    @Test
    @DirtiesContext
    void testShouldSaveItem() {
        userService.save(user);
        itemService.save(itemDto, 1L);

        TypedQuery<Item> query = entityManager.createQuery("select i from Item i where i.id = :id", Item.class);
        Item item = query.setParameter("id", 1L).getSingleResult();
        assertThat("Неправильный результат id", item.getId(), notNullValue());
    }

    @Test
    @DirtiesContext
    void testShouldUpdateItem() {
        userService.save(user);
        itemService.save(itemDto, 1L);

        Item update = Item.builder().name("updatedName").build();
        itemService.update(update, 1L, 1L);

        TypedQuery<Item> query = entityManager.createQuery("select i from Item i where i.id = :id", Item.class);
        Item updatedItem = query.setParameter("id", 1L).getSingleResult();

        assertThat("Неправильный результат название", updatedItem.getName(), equalTo("updatedName"));
    }

    @Test
    @DirtiesContext
    void testShouldSaveComment() {
        userService.save(user);
        userService.save(secondUser);
        itemService.save(itemDto, 1L);
        bookingService.save(1L, LocalDateTime.now(), MAX, 2L);

        String text = "text";
        itemService.saveComment(1L, 2L, text);

        TypedQuery<Comment> query = entityManager.createQuery(
                "select c from Comment c where c.id = :id", Comment.class);

        Comment comment = query.setParameter("id", 1L).getSingleResult();
        item.setId(1L);
        assertThat("Неправильный результат id", comment.getId(), notNullValue());
        assertThat("Неправильный результат текст", comment.getText(), equalTo(text));
    }

    @Test
    @DirtiesContext
    void testShouldFindById() {
        userService.save(user);
        itemService.save(itemDto, 1L);
        ItemAllFieldsDto itemAllFieldsDto = itemService.findById(1L, 1L);

        TypedQuery<Item> query = entityManager.createQuery("select i from Item i where i.id = :id", Item.class);

        Item itemFromBd = query.setParameter("id", 1L).getSingleResult();

        assertThat("Неправильный результат название", itemAllFieldsDto.getName(), equalTo(itemFromBd.getName()));
        assertThat("Неправильный результат описание", itemAllFieldsDto.getDescription(), equalTo(itemFromBd.getDescription()));
        assertThat("Неправильный результат доступа", itemAllFieldsDto.getAvailable(), equalTo(itemFromBd.getAvailable()));
    }

    @Test
    @DirtiesContext
    void testShouldFindItemsByUserId() {
        userService.save(user);
        itemService.save(itemDto, 1L);

        Collection<ItemAllFieldsDto> items = itemService.findItemsByUserId(1L, page);

        assertThat("Неправильный результат размер", items.size(), equalTo(1));
    }

    @Test
    @DirtiesContext
    void testShouldSearchByText() {
        userService.save(user);
        itemDto.setDescription("toSearch");
        itemService.save(itemDto, 1L);

        List<ItemAllFieldsDto> items = (List<ItemAllFieldsDto>)
                itemService.searchByText("toSearch", 1L, page);

        assertThat("Неправильный результат размер", items.size(), equalTo(1));
        assertThat("Неправильный результат описание", items.get(0).getDescription(), equalTo("toSearch"));
    }
}

