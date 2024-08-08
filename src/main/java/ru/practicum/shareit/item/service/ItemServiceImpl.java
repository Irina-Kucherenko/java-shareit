package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.exception.UserNotOwnerException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final UserService userService;

    @Override
    public boolean checkItem(Long itemId) {
        return itemRepository.getItemById(itemId) != null;
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, Long userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User id must be greater than 0");
        } else if (!(userService.checkUser(userId))) {
            throw new IllegalArgumentException("User with id " + userId + " already exists.");
        } else {
            Item item = ItemMapper.transformToItem(itemDto);
            item.setOwnerId(userId);
            itemRepository.addItem(item);
            return ItemMapper.transformToDto(item);
        }

    }

    @Override
    public ItemDto updateItem(Long itemId, ItemDto itemDto, Long userId) {
        if (userService.checkUser(userId)) {
            Item updatedItem = itemRepository.getItemById(itemId);
            Item item = ItemMapper.transformToItem(itemDto);
            if (updatedItem.getOwnerId().equals(userId)) {
                if (item.getName() != null) {
                    updatedItem.setName(item.getName());
                }
                if (item.getDescription() != null) {
                    updatedItem.setDescription(item.getDescription());
                }
                if (item.getAvailable() != null) {
                    updatedItem.setAvailable(item.getAvailable());
                }
                itemRepository.updateItem(updatedItem);
                return ItemMapper.transformToDto(updatedItem);
            } else {
                throw new UserNotOwnerException("You are not owner of this item.");
            }
        }
        throw new ResourceNotFoundException("User not found");
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        if (checkItem(itemId)) {
            Item item = itemRepository.getItemById(itemId);
            return ItemMapper.transformToDto(item);
        }
        throw new ResourceNotFoundException("Item not found");
    }

    @Override
    public List<ItemDto> getItemsFromUser(Long userId) {
        if (userService.checkUser(userId)) {
            List<Item> items = itemRepository.getItemsFromUser(userId);
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
        List<Item> items = itemRepository.searchItems(text);

        return items.stream().map(ItemMapper::transformToDto).filter(ItemDto::getAvailable).toList();
    }
}
