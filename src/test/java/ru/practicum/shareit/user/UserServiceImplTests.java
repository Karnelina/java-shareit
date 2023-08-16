package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTests {
    @Mock
    private UserRepository mockUserRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void init() {
        user = User.builder()
                .id(1L)
                .name("name")
                .email("email@email.ru")
                .build();
    }

    @Test
    void testShouldSaveUser() {
        when(mockUserRepository.save(Mockito.any(User.class)))
                .thenReturn(user);

        User userForBd = user;
        userForBd.setId(null);

        assertThat("Неправильный результат", userService.save(userForBd), equalTo(user));
        verify(mockUserRepository, times(1)).save(any());
    }

    @Test
    void testShouldThrowExceptionWhenSavingUserWithDuplicateEmail() {
        when(mockUserRepository.save(Mockito.any(User.class)))
                .thenThrow(DataIntegrityViolationException.class);

        User duplicateUser = User.builder()
                .name("duplicate")
                .email(user.getEmail())
                .build();

        assertThrows(AlreadyExistsException.class,
                () -> userService.save(duplicateUser), "Ошибка отработала неправильно");

        verify(mockUserRepository, times(1)).save(any());
    }

    @Test
    void testShouldFindByIdUser() {
        Long userId = user.getId();

        when(mockUserRepository.findById(userId))
                .thenReturn(Optional.ofNullable(user));

        assertThat("Неправильный результат", user, equalTo(userService.findById(userId)));
        verify(mockUserRepository, times(1)).findById(userId);
    }

    @Test
    void testShouldThrowExceptionWhenNotFoundFindById() {
        long userId = 0L;
        when(mockUserRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findById(userId)
                , "Ошибка отработала неправильно");
        verify(mockUserRepository, times(1)).findById(any());
    }

    @Test
    void testShouldFindAllUsers() {
        when(mockUserRepository.findAll())
                .thenReturn(List.of(user));

        assertThat("Неправильный результат", userService.findAll().size(), equalTo(1));
        verify(mockUserRepository, times(1)).findAll();
    }

    @Test
    void testShouldThrowExceptionWhenUserNotFoundOnUpdate() {
        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.update(user, 0L)
                , "Ошибка отработала неправильно");
        verify(mockUserRepository, times(1)).findById(0L);
    }

    @Test
    void testShouldChangeUserNameOnUpdate() {
        User update = User.builder()
                .name("update")
                .build();

        User updatedUser = user;
        updatedUser.setName("updated");

        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));

        when(mockUserRepository.saveAndFlush(Mockito.any(User.class)))
                .thenReturn(updatedUser);

        assertThat("Неправильный результат", userService.update(update, user.getId()), equalTo(updatedUser));
        verify(mockUserRepository, times(1)).saveAndFlush(updatedUser);
    }

    @Test
    void testShouldChangeUserEmailOnUpdate() {
        User update = User.builder()
                .email("update@email.ru")
                .build();

        User updatedUser = user;
        updatedUser.setEmail("update@email.ru");

        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));

        when(mockUserRepository.saveAndFlush(Mockito.any(User.class)))
                .thenReturn(updatedUser);

        assertThat("Неправильный результат", userService.update(update, user.getId()), equalTo(updatedUser));
        verify(mockUserRepository, times(1)).saveAndFlush(updatedUser);
    }

    @Test
    void testShouldThrowExceptionWhenUserAlreadyExistsOnUpdate() {
        User update = User.builder()
                .name("update")
                .email("update@email.ru")
                .build();

        User updatedUser = user;
        updatedUser.setName("update");
        updatedUser.setEmail("update@email.ru");

        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        when(mockUserRepository.saveAndFlush(Mockito.any(User.class)))
                .thenThrow(DataIntegrityViolationException.class);

        long userId = user.getId();
        assertThrows(AlreadyExistsException.class, () -> userService.update(update, userId),
                "Ошибка отработала неправильно");
        verify(mockUserRepository, times(1)).saveAndFlush(updatedUser);
    }

    @Test
    void testShouldDeleteUserById() {
        userService.deleteById(user.getId());
        verify(mockUserRepository, times(1)).deleteById(user.getId());
    }
}
