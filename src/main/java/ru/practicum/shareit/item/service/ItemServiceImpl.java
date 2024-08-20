package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ExecutionNotPossibleException;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final UserService userService;

    private final CommentRepository commentRepository;

    private final BookingRepository bookingRepository;

    @Override
    public boolean checkItem(Long itemId) {
        return itemRepository.existsById(itemId);
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, Long userId) {
        if (!(userService.checkUser(userId))) {
            throw new ResourceNotFoundException("User with id " + userId + " not found");
        } else {
            Item item = ItemMapper.transformToItem(itemDto);
            item.setOwner(UserMapper.transformToUser(userService.getUserById(userId)));
            itemRepository.save(item);
            return ItemMapper.transformToDto(item);
        }
    }

    @Override
    public ItemDto updateItem(Long itemId, ItemDto itemDto, Long userId) {
        if (userService.checkUser(userId)) {
            Item updatedItem = itemRepository.getReferenceById(itemId);
            Item item = ItemMapper.transformToItem(itemDto);
            if (item.getName() != null) {
                updatedItem.setName(item.getName());
            }
            if (item.getDescription() != null) {
                updatedItem.setDescription(item.getDescription());
            }
            if (item.getAvailable() != null) {
                updatedItem.setAvailable(item.getAvailable());
            }
            itemRepository.save(updatedItem);
            return ItemMapper.transformToDto(updatedItem);

        }
        throw new ResourceNotFoundException("User not found");
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        if (checkItem(itemId)) {
            Item item = itemRepository.getReferenceById(itemId);
            ItemDto itemDto = ItemMapper.transformToDto(item);
            itemDto.setComments(commentRepository.findAllByItemId(itemId).
                    stream().map(CommentMapper::transformToDto).toList());
            return itemDto;
        }
        throw new ResourceNotFoundException("Item not found");
    }

    @Override
    public List<ItemDto> getItemsFromUser(Long userId) {
        if (userService.checkUser(userId)) {
            List<Item> items = itemRepository.findItemsByOwnerId(userId);
            return items.stream().map(ItemMapper::transformToDto).toList();
        }
        throw new ResourceNotFoundException("User not found");
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Text must contain only alphanumeric characters");
        }
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        List<Item> items = itemRepository.searchItemsByText(text);

        return items.stream().map(ItemMapper::transformToDto).filter(ItemDto::getAvailable).toList();
    }

    @Override
    public CommentDto addComment(Comment comment, Long userId, Long itemId) {
        if (!(itemRepository.existsById(itemId))) {
            throw new ResourceNotFoundException("Item with id " + itemId + " not found");
        }
        if (!(userService.checkUser(userId))) {
            throw new ResourceNotFoundException("User not found");
        }
        Optional<Booking> bookingOptional = bookingRepository.findFirstByBookerIdAndEndBeforeAndStatusNot(userId,
                LocalDateTime.now(), BookingStatus.REJECTED);
        if (bookingOptional.isEmpty()) {
            throw new ExecutionNotPossibleException("Can not add comment, because there was no booking.");
        }
        User author = UserMapper.transformToUser(userService.getUserById(userId));
        Item item = itemRepository.getReferenceById(itemId);
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());
        commentRepository.save(comment);
        return CommentMapper.transformToDto(comment);
    }
}
