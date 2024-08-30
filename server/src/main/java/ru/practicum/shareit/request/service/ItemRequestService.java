package ru.practicum.shareit.request.service;


import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    boolean checkItemRequest(Long requestId);

    ItemRequestDto addItemRequest(Long userId, ItemRequestDto request);

    ItemRequestDto getParticularRequest(Long requestId);

    List<ItemRequestDto> getOwnRequests(Long userId);

    List<ItemRequestDto> getAllRequestsByUserId(Long userId);


}
