package ru.practicum.shareit.booking.service;


import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {

    BookingDto addBooking(BookingRequestDto bookingRequestDto, Long bookerId);

    BookingDto approveBooking(Long userId, Long bookingId, boolean approved);

    BookingDto getParticularBookingOfUser(Long userId, Long bookingId);

    List<BookingDto> getItemBookingsByOwner(Long ownerId, BookingState bookingState);

    List<BookingDto> getBookingsByUser(Long userId, BookingState bookingState);

    boolean checkBooking(Long bookingId);
}
