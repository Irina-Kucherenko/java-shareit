package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository(value = "bookingRepos")
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findFirstByBookerIdAndEndBeforeAndStatusNot(long bookerId, LocalDateTime end, BookingStatus status);

    List<Booking> getAllByBookerId(Long bookerId);

    List<Booking> getAllByItemOwnerId(Long ownerId);

    @Query("SELECT booking" +
            " FROM Booking booking" +
            " WHERE booking.booker.id = :bookerId AND booking.end < CURRENT_TIMESTAMP" +
            " ORDER BY booking.start DESC")
    List<Booking> getPastBookingsByBookerId(@Param("bookerId") Long bookerId);

    @Query("SELECT booking" +
            " FROM Booking booking" +
            " WHERE booking.item.owner.id = :ownerId" +
            " AND booking.end < CURRENT_TIMESTAMP" +
            " ORDER BY booking.start DESC")
    List<Booking> getPastBookingsByOwnerId(@Param("ownerId") Long ownerId);

    @Query("SELECT booking " +
            "FROM Booking booking" +
            " WHERE booking.booker.id = :bookerId" +
            " AND booking.start <= CURRENT_TIMESTAMP" +
            " AND booking.end >= CURRENT_TIMESTAMP" +
            " ORDER BY booking.start DESC")
    List<Booking> getPresentBookingsByBookerId(@Param("bookerId") Long bookerId);

    @Query("SELECT booking" +
            " FROM Booking booking" +
            " WHERE booking.item.owner.id = :ownerId" +
            " AND booking.start < CURRENT_TIMESTAMP" +
            " AND booking.end > CURRENT_TIMESTAMP" +
            " ORDER BY booking.start DESC")
    List<Booking> getPresentBookingsByOwnerId(@Param("ownerId") Long ownerId);

    @Query("SELECT booking" +
            " FROM Booking booking" +
            " WHERE booking.booker.id = :bookerId" +
            " AND booking.start > CURRENT_TIMESTAMP" +
            " ORDER BY booking.start DESC")
    List<Booking> getAheadBookingsByBookerId(@Param("bookerId") Long bookerId);

    @Query("SELECT booking" +
            " FROM Booking booking" +
            " WHERE booking.item.owner.id = :ownerId" +
            " AND booking.start > CURRENT_TIMESTAMP" +
            " ORDER BY booking.start DESC")
    List<Booking> getAheadBookingsByOwnerId(@Param("ownerId") Long ownerId);

    @Query("SELECT booking" +
            " FROM Booking booking" +
            " WHERE booking.booker.id = :bookerId" +
            " AND booking.status = :status" +
            " ORDER BY booking.start DESC")
    List<Booking> getBookingsByBookerIdAndStatus(@Param("bookerId") Long bookerId,
                                                 @Param("status") BookingStatus status);

    @Query("SELECT booking" +
            " FROM Booking booking" +
            " WHERE booking.item.owner.id = :ownerId" +
            " AND booking.status = :status" +
            " ORDER BY booking.start DESC")
    List<Booking> getBookingsByOwnerIdAndStatus(@Param("ownerId") Long ownerId,
                                                @Param("status") BookingStatus status);

}
