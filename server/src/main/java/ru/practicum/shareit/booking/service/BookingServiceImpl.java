package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.InvalidBookingException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private static final String USER_NOT_FOUND = "User not found";

    private final BookingRepository bookingRepository;

    private final UserService userService;

    private final ItemService itemService;

    @Override
    public boolean checkBooking(Long bookingId) {
        return bookingRepository.existsById(bookingId);
    }

    @Override
    @Transactional
    public BookingDto addBooking(BookingRequestDto bookingRequestDto, Long bookerId) {
        if (!userService.checkUser(bookerId)) {
            throw new InvalidBookingException(USER_NOT_FOUND);
        }
        Item item = ItemMapper.transformToItem(itemService.getItemById((bookingRequestDto.getItemId())));
        if (!(item.getAvailable())) {
            throw new InvalidBookingException("Item is not available");
        }
        User booker = UserMapper.transformToUser(userService.getUserById(bookerId));
        Booking booking = BookingMapper.transformToBookingFromBookingRequestDto(bookingRequestDto,
                item, booker, BookingStatus.WAITING);
        bookingRepository.save(booking);
        return BookingMapper.transformToDto(booking);
    }

    @Override
    @Transactional
    public BookingDto approveBooking(Long userId, Long bookingId, boolean approved) {
        if (!(checkBooking(bookingId))) {
            throw new InvalidBookingException("Booking not found");
        }
        Booking booking = bookingRepository.getReferenceById(bookingId);
        if (!(userId.equals(booking.getItem().getOwner().getId()))) {
            throw new NotOwnerException("Not owner of booking");
        }
        BookingStatus status = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        booking.setStatus(status);
        return BookingMapper.transformToDto(booking);

    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto getParticularBookingOfUser(Long userId, Long bookingId) {
        if (!userService.checkUser(userId)) {
            throw new ResourceNotFoundException(USER_NOT_FOUND);
        }
        if (!(checkBooking(bookingId))) {
            throw new ResourceNotFoundException("Booking not found");
        }
        Booking booking = bookingRepository.getReferenceById(bookingId);
        return BookingMapper.transformToDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getItemBookingsByOwner(Long ownerId, BookingState bookingState) {
        if (!(userService.checkUser(ownerId))) {
            throw new ResourceNotFoundException(USER_NOT_FOUND);
        }
        List<Booking> bookings = switch (bookingState) {
            case PAST -> bookingRepository.getPastBookingsByOwnerId(ownerId);
            case PRESENT -> bookingRepository.getPresentBookingsByOwnerId(ownerId);
            case FUTURE -> bookingRepository.getAheadBookingsByOwnerId(ownerId);
            case WAITING -> bookingRepository.getBookingsByOwnerIdAndStatus(ownerId, BookingStatus.WAITING);
            case REJECTED -> bookingRepository.getBookingsByOwnerIdAndStatus(ownerId, BookingStatus.REJECTED);
            default -> bookingRepository.getAllByItemOwnerId(ownerId);
        };
        return bookings.stream().map(BookingMapper::transformToDto).toList();

    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getBookingsByUser(Long bookerId, BookingState bookingState) {
        if (!(userService.checkUser(bookerId))) {
            throw new ResourceNotFoundException(USER_NOT_FOUND);
        }
        List<Booking> bookings = switch (bookingState) {
            case PAST -> bookingRepository.getPastBookingsByBookerId(bookerId);
            case PRESENT -> bookingRepository.getPresentBookingsByBookerId(bookerId);
            case FUTURE -> bookingRepository.getAheadBookingsByBookerId(bookerId);
            case WAITING -> bookingRepository.getBookingsByBookerIdAndStatus(bookerId, BookingStatus.WAITING);
            case REJECTED -> bookingRepository.getBookingsByBookerIdAndStatus(bookerId, BookingStatus.REJECTED);
            default -> bookingRepository.getAllByBookerId(bookerId);
        };
        return bookings.stream().map(BookingMapper::transformToDto).toList();
    }
}
