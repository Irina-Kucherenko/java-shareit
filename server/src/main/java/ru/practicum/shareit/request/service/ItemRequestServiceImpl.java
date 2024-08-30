package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private static final String USER_NOT_FOUND = "User not found";

    private final ItemRequestRepository itemRequestRepository;

    private final UserService userService;

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;


    @Override
    public boolean checkItemRequest(Long requestId) {
        return itemRequestRepository.existsById(requestId);
    }

    @Override
    public ItemRequestDto addItemRequest(Long userId, ItemRequestDto request) {
        if (!userService.checkUser(userId)) {
            throw new ResourceNotFoundException(USER_NOT_FOUND);
        }
        ItemRequest itemRequest = ItemRequestMapper.transformToItem(request);
        itemRequest.setRequester(userRepository.getReferenceById(userId));
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest = itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.transformToDto(itemRequest);
    }

    @Override
    public ItemRequestDto getParticularRequest(Long requestId) {
        if (!checkItemRequest(requestId)) {
            throw new ResourceNotFoundException("Request not found");
        }
        return getRequestDtoWithData(itemRequestRepository.getReferenceById(requestId));
    }

    @Override
    public List<ItemRequestDto> getOwnRequests(Long userId) {
        if (!userService.checkUser(userId)) {
            throw new ResourceNotFoundException(USER_NOT_FOUND);
        }
        return getRequestsDtoWithData(itemRequestRepository.findByOwnerId(userId));
    }

    @Override
    public List<ItemRequestDto> getAllRequestsByUserId(Long userId) {
        return getRequestsDtoWithData(itemRequestRepository.findByOwnerIdNotEquals(userId));
    }

    private ItemRequestDto getRequestDtoWithData(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = ItemRequestMapper.transformToDto(itemRequest);
        itemRequestDto.setItems(itemRepository.findByRequestId(itemRequest.getId()).stream()
                .map(ItemMapper::transformToDto).toList());
        return itemRequestDto;
    }

    private List<ItemRequestDto> getRequestsDtoWithData(List<ItemRequest> itemRequests) {
        return itemRequests.stream()
                .map(this::getRequestDtoWithData).toList();
    }
}