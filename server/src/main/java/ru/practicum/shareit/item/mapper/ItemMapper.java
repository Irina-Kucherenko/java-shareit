package ru.practicum.shareit.item.mapper;


import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

public class ItemMapper {

    public static ItemDto transformToDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setRequestId(item.getRequest() == null ? null : item.getRequest().getId());
        return itemDto;
    }

    public static Item transformToItem(ItemDto itemDto) {
        Item item = new Item();
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(itemDto.getRequestId());
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setRequest(itemDto.getRequestId() == null ? null : itemRequest);
        return item;
    }
}
