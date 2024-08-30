package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto addItemRequest(@RequestBody ItemRequestDto requestDto,
                                         @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.addItemRequest(userId, requestDto);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getParticularRequest(@PathVariable Long requestId) {
        return itemRequestService.getParticularRequest(requestId);
    }

    @GetMapping
    public List<ItemRequestDto> getOwnRequests(@RequestHeader("X-Sharer-User-Id") Long requesterId) {
        return itemRequestService.getOwnRequests(requesterId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequestsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getAllRequestsByUserId(userId);
    }
}
