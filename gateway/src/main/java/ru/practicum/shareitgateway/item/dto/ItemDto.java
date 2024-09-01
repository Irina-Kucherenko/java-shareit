package ru.practicum.shareitgateway.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareitgateway.booking.dto.BookingItemResponseDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private  Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Boolean available;

    private Long ownerId;

    private BookingItemResponseDto lastBooking;

    private BookingItemResponseDto nextBooking;

    private Long requestId;

    private List<CommentDto> comments;
}
