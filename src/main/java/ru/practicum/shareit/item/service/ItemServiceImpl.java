package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.GetItemBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemAllFieldsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.shareit.util.Constants.orderByStartDateAsc;
import static ru.practicum.shareit.util.Constants.orderByStartDateDesc;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository requestRepository;

    @Override
    public Item save(ItemDto itemDto, Long ownerId) {
        Item item = ItemMapper.INSTANCE.mapToItem(itemDto);

        User owner = userRepository.findById(ownerId).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь %s не найден.", ownerId)));

        item.setOwner(owner);

        if (itemDto.getRequestId() != null) {
            ItemRequest itemRequest = requestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() ->
                            new NotFoundException(String.format("Запрос %s не найден", itemDto.getRequestId())));
            item.setItemRequest(itemRequest);
        }

        return itemRepository.save(item);
    }

    @Override
    public Item update(Item item, Long itemId, Long userId) {
        Item updatedItem = itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException(String.format("Предмет не найден: %s", item)));

        if (!Objects.equals(updatedItem.getOwner().getId(), userId)) {
            throw new NotFoundException(
                    String.format("У пользователя %d нету предмета %s.", userId, item));
        }

        if (item.getName() != null && !item.getName().isBlank()) {
            updatedItem.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            updatedItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updatedItem.setAvailable(item.getAvailable());
        }

        itemRepository.save(updatedItem);

        return updatedItem;
    }

    @Override
    @Transactional(readOnly = true)
    public ItemAllFieldsDto findById(Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException(String.format("Item %s не найден.", itemId)));

        return findItemsDto(Collections.singletonList(item), userId).get(0);
    }

    private List<ItemAllFieldsDto> findItemsDto(List<Item> items, Long userId) {
        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }

        List<Booking> bookings;

        if (items.size() == 1) {
            bookings = bookingRepository.findBookingsByItemId(items, userId, Status.APPROVED);
        } else {
            bookings = bookingRepository.findBookingsByItemIn(items);
        }

        List<Comment> comments = commentRepository.findCommentsByItemInOrderByCreated(items);

        Map<Item, List<Booking>> bookingsMap = bookings.stream()
                .collect(Collectors.groupingBy(Booking::getItem));

        Map<Item, List<Comment>> commentsMap = comments.stream()
                .collect(Collectors.groupingBy(Comment::getItem));

        return items.stream()
                .map(item -> {
                    List<CommentResponseDto> itemComments = commentsMap
                            .getOrDefault(item, Collections.emptyList())
                            .stream()
                            .map(CommentMapper.INSTANCE::mapToCommentResponseDto)
                            .collect(Collectors.toList());

                    List<Booking> itemBookings = bookingsMap.getOrDefault(item, Collections.emptyList());

                    Optional<Booking> lastOptional = getLastItem(itemBookings);
                    Optional<Booking> nextOptional = getNextItem(itemBookings);

                    GetItemBookingDto last = lastOptional.map(BookingMapper.INSTANCE::mapFromBookingToBookingDto).orElse(null);
                    GetItemBookingDto next = nextOptional.map(BookingMapper.INSTANCE::mapFromBookingToBookingDto).orElse(null);

                    return ItemMapper.INSTANCE.mapToItemAllFieldsDto(item, last, next, itemComments);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemAllFieldsDto> searchByText(String text, Long userId, Pageable page) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }

        List<Item> items = itemRepository.findItemsByText(text, page);
        return findItemsDto(items, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemAllFieldsDto> findItemsByUserId(Long userId, Pageable page) {
        List<Item> items = itemRepository.findAllByOwnerId(userId, page);
        return findItemsDto(items, userId);
    }

    @Override
    public CommentResponseDto saveComment(Long itemId, Long userId, String text) {
        if (text.isBlank()) {
            throw new ValidationException("Комментарий не может быть пустым");
        }

        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь %s не найден.", userId)));

        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException(String.format("Item %s не найден.", itemId)));

        if (bookingRepository.findByBooker(user, Sort.unsorted()).isEmpty()) {
            throw new ValidationException(String.format("У пользователя %s нету бронирований", user.getId()));
        }

        List<Booking> bookings = bookingRepository.findBookingByItemIdAndStatusNotInAndStartBefore(itemId,
                List.of(Status.REJECTED), LocalDateTime.now());
        if (bookings == null || bookings.isEmpty()) {
            throw new ValidationException("Требуется бронирование для создания комментария ");
        }

        Comment comment = Comment.builder()
                .text(text)
                .created(LocalDateTime.now())
                .item(item)
                .author(user)
                .build();

        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.INSTANCE.mapToCommentResponseDto(savedComment);
    }

    private Optional<Booking> getNextItem(List<Booking> bookings) {
        LocalDateTime currentTime = LocalDateTime.now();
        return bookings.stream()
                .sorted(orderByStartDateAsc)
                .filter(t -> t.getStart().isAfter(currentTime) &&
                        t.getStatus().equals(Status.APPROVED))
                .findFirst();
    }

    private Optional<Booking> getLastItem(List<Booking> bookings) {
        LocalDateTime currentTime = LocalDateTime.now();
        return bookings.stream()
                .sorted(orderByStartDateDesc)
                .filter(t -> t.getStart().isBefore(currentTime) &&
                        t.getStatus().equals(Status.APPROVED))
                .findFirst();
    }

}
