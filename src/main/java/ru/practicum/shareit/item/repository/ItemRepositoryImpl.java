package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@Qualifier("itemRepos")
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();

    private Long idCounter = 1L;

    @Override
    public Item addItem(Item item) {
        item.setId(idCounter);
        items.put(item.getId(), item);
        idCounter++;
        log.info("Added item: {}", item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        log.info("Updated item: {}", item);
        return item;
    }

    @Override
    public Item getItemById(Long itemId) {
        if (items.containsKey(itemId)) {
            log.info("Found item: {}", itemId);
            return items.get(itemId);
        } else {
            throw new ResourceNotFoundException("Item not found");
        }
    }

    @Override
    public List<Item> getItemsFromUser(Long userId) {
        log.info("Retrieving items from user: {}", userId);
        return items.values().stream()
                .filter(item -> item.getOwnerId().equals(userId))
                .toList();
    }

    @Override
    public List<Item> searchItems(String text) {
        log.info("Searching for items with text {}", text);
        return items.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase()) &&
                                item.getAvailable()).toList();
    }
}
