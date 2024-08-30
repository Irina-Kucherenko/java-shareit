package ru.practicum.shareitgateway.booking.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareitgateway.booking.BookingClient;
import ru.practicum.shareitgateway.booking.dto.BookingItemRequestDto;
import ru.practicum.shareitgateway.booking.dto.BookingState;


@Controller
@RequestMapping(path = "bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                @RequestBody @Valid BookingItemRequestDto booking) {
        return bookingClient.addBooking(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBooking(@RequestHeader("X-Sharer-User-Id") @Positive Long ownerId,
                                                @PathVariable @Positive Long bookingId, @RequestParam boolean approved) {
        return bookingClient.updateBooking(ownerId, bookingId, approved);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsByOwnerId(@RequestHeader("X-Sharer-User-Id") @Positive Long ownerId,
                                                       @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingClient.getBookingsByOwnerId(ownerId, state);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsByBooker(@RequestHeader("X-Sharer-User-Id") @Positive Long bookerId,
                                                      @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingClient.getBookingsByBookerId(bookerId, state);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                 @PathVariable @Positive Long bookingId) {
        return bookingClient.getParticularBookingById(userId, bookingId);
    }
}
