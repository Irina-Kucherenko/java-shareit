package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ItemDto addItem(@Valid @RequestBody ItemDto itemDto,
                           @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        return itemService.addItem(itemDto, userId);
    }

    @PatchMapping(value = "/{itemId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ItemDto updateItem(@PathVariable Long itemId,
                              @RequestBody ItemDto itemDto,
                              @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("Updating item with id {}", itemId);
        return itemService.updateItem(itemId, itemDto, userId);
    }

    @GetMapping(value = "/{itemId}", produces = APPLICATION_JSON_VALUE)
    public ItemDto getItemById(@PathVariable Long itemId) {
        log.info("Retrieving item with id {}", itemId);
        return itemService.getItemById(itemId);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<ItemDto> getItemsFromUser(@RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("Retrieving items from user {}", userId);
        return itemService.getItemsFromUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam(name = "text") String text) {
        log.info("Searching for items with text {}", text);
        return itemService.searchItems(text);
    }
}
