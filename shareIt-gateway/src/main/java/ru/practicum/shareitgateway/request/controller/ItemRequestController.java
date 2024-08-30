package ru.practicum.shareitgateway.request.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareitgateway.request.ItemRequestClient;
import ru.practicum.shareitgateway.request.dto.ItemRequestDto;

@RequiredArgsConstructor
@Slf4j
@Validated
@RestController
@RequestMapping("requests")
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                             @RequestBody @Valid ItemRequestDto request) {
        return itemRequestClient.addRequest(userId, request);
    }

    @GetMapping
    public ResponseEntity<Object> getOwnRequests(@RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return itemRequestClient.getOwnRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequestsByUserId(@RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return itemRequestClient.getAllRequestsByUserId(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getParticularRequest(@PathVariable @Positive Long requestId) {
        return itemRequestClient.getParticularRequest(requestId);
    }
}
