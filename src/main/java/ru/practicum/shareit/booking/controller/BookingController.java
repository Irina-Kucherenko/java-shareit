package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public BookingDto addBooking(@RequestHeader(name = "X-Sharer-User-Id") Long bookerId,
                                 @RequestBody @Valid BookingRequestDto bookingDtoRequest) {
        log.info("Creating new booking for booker id {}", bookerId);
        return bookingService.addBooking(bookingDtoRequest, bookerId);
    }

    @PatchMapping(value = "/{bookingId}")
    public BookingDto approveBooking(@RequestHeader(name = "X-Sharer-User-Id") Long ownerId,
                                     @PathVariable Long bookingId, @RequestParam boolean approved) {
        log.info("Updating approval for booking id {}", bookingId);
        return bookingService.approveBooking(ownerId, bookingId, approved);
    }

    @GetMapping(value = "/{bookingId}", produces = APPLICATION_JSON_VALUE)
    public BookingDto getParticularBookingOfUser(@RequestHeader(name = "X-Sharer-User-Id") Long bookerId,
                                                 @PathVariable Long bookingId) {
        log.info("Getting particular booking of user id {}", bookerId);
        return bookingService.getParticularBookingOfUser(bookerId, bookingId);
    }

    @GetMapping(value = "/owner", produces = APPLICATION_JSON_VALUE)
    public List<BookingDto> getItemBookingsByOwner(@RequestHeader(name = "X-Sharer-User-Id") Long ownerId,
                                                   @RequestParam(name = "state", defaultValue = "ALL")
                                                   BookingState state) {
        log.info("Getting bookings by items of owner id {}", ownerId);
        return bookingService.getItemBookingsByOwner(ownerId, state);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<BookingDto> getAllUserBookings(@RequestHeader(name = "X-Sharer-User-Id") Long bookerId,
                                               @RequestParam(name = "state", defaultValue = "ALL")
                                               BookingState state) {
        log.info("Getting bookings by user id {}", bookerId);
        return bookingService.getBookingsByUser(bookerId, state);
    }
}


