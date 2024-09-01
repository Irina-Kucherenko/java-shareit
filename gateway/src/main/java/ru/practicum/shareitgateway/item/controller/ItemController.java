package ru.practicum.shareitgateway.item.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareitgateway.item.ItemClient;
import ru.practicum.shareitgateway.item.dto.CommentDto;
import ru.practicum.shareitgateway.item.dto.ItemDto;

@Controller
@RequestMapping(path = "items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") @Positive Long ownerId,
                                             @RequestBody @Valid ItemDto item) {
        return itemClient.addItem(ownerId, item);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") @Positive Long ownerId,
                                             @PathVariable @Positive Long itemId,
                                             @RequestBody @Valid ItemDto itemDto) {
        return itemClient.updateItem(ownerId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable @Positive Long itemId) {
        return itemClient.getItemById(itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByOwnerId(@RequestHeader("X-Sharer-User-Id") @Positive Long ownerId) {
        return itemClient.getItemsByOwnerId(ownerId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItemByText(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                   @RequestParam(defaultValue = "") String text) {
        return itemClient.searchItemByText(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                @PathVariable @Positive Long itemId,
                                                @RequestBody @Valid CommentDto comment) {
        return itemClient.addComment(userId, itemId, comment);
    }
}
