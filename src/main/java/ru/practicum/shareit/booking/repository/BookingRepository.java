package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends PagingAndSortingRepository<Booking, Long> {
    // Методы получения бронирований для владельца
    List<Booking> findByItem_Owner_Id(Long id, Pageable pageable);

    @Query("select b from Booking b " +
            "where b.item.owner.id = :ownerId " +
            "and b.start <= :currentDate " +
            "and b.end >= :currentDate")
    List<Booking> findByOwnerIdCurrentBookings(@Param("ownerId") Long ownerId, @Param("currentDate") LocalDateTime currentDate,
                                               Pageable pageable);

    @Query("select b from Booking b " +
            "where b.item.owner.id = :ownerId " +
            "and b.end < :currentDate")
    List<Booking> findByOwnerIdPastBookings(@Param("ownerId") Long ownerId, @Param("currentDate") LocalDateTime currentDate,
                                            Pageable pageable);

    @Query("select b from Booking b " +
            "where b.item.owner.id = :ownerId " +
            "and b.end > :currentDate")
    List<Booking> findByOwnerIdFutureBookings(@Param("ownerId") Long ownerId, @Param("currentDate") LocalDateTime currentDate,
                                              Pageable pageable);

    List<Booking> findByItem_Owner_IdAndStatus(Long ownerId, Booking.Status status, Pageable pageable);

    // Методы получения бронирований для заказчика
    List<Booking> findByBooker_Id(Long bookerId, Pageable pageable);

    @Query("select b from Booking b " +
            "where b.booker.id = :bookerId " +
            "and b.start <= :currentDate " +
            "and b.end >= :currentDate")
    List<Booking> findByBookerCurrentBookings(@Param("bookerId") Long bookerId, @Param("currentDate") LocalDateTime currentDate,
                                              Pageable pageable);

    @Query("select b from Booking b " +
            "where b.booker.id = :bookerId " +
            "and b.end < :currentDate")
    List<Booking> findByBookerPastBookings(@Param("bookerId") Long bookerId, @Param("currentDate") LocalDateTime currentDate,
                                           Pageable pageable);

    @Query("select b from Booking b " +
            "where b.booker.id = :bookerId " +
            "and b.end > :currentDate")
    List<Booking> findByBookerFutureBookings(@Param("bookerId") Long bookerId, @Param("currentDate") LocalDateTime currentDate,
                                             Pageable pageable);

    List<Booking> findByBooker_IdAndStatus(Long bookerId, Booking.Status status, Pageable pageable);

    // Получение двух последних бронирований для владельца вещи
    @Query("select b from Booking b " +
            "where b.item.owner.id = :ownerId " +
            "and b.item.id = :itemId")
    Page<Booking> getLastBookings(@Param("ownerId") Long ownerId, @Param("itemId") Long itemId, Pageable pageable);
}