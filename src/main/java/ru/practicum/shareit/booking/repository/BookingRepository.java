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
    List<Booking> findByItem_Owner_IdOrderByEndDesc(Long id);

    @Query("select b from Booking b " +
            "where b.item.owner.id = :ownerId " +
            "and b.start <= :currentDate " +
            "and b.end >= :currentDate " +
            "order by b.end desc")
    List<Booking> findByOwnerIdCurrentBookings(@Param("ownerId") Long ownerId, @Param("currentDate") LocalDateTime currentDate);

    @Query("select b from Booking b " +
            "where b.item.owner.id = :ownerId " +
            "and b.end < :currentDate " +
            "order by b.end desc")
    List<Booking> findByOwnerIdPastBookings(@Param("ownerId") Long ownerId, @Param("currentDate") LocalDateTime currentDate);

    @Query("select b from Booking b " +
            "where b.item.owner.id = :ownerId " +
            "and b.end > :currentDate " +
            "order by b.end desc")
    List<Booking> findByOwnerIdFutureBookings(@Param("ownerId") Long ownerId, @Param("currentDate") LocalDateTime currentDate);

    List<Booking> findByItem_Owner_IdAndStatusOrderByEndDesc(Long ownerId, Booking.Status status);

    // Методы получения бронирований для заказчика
    List<Booking> findByBooker_IdOrderByEndDesc(Long bookerId);

    @Query("select b from Booking b " +
            "where b.booker.id = :bookerId " +
            "and b.start <= :currentDate " +
            "and b.end >= :currentDate " +
            "order by b.end desc")
    List<Booking> findByBookerCurrentBookings(@Param("bookerId") Long bookerId, @Param("currentDate") LocalDateTime currentDate);

    @Query("select b from Booking b " +
            "where b.booker.id = :bookerId " +
            "and b.end < :currentDate " +
            "order by b.end desc")
    List<Booking> findByBookerPastBookings(@Param("bookerId") Long bookerId, @Param("currentDate") LocalDateTime currentDate);

    @Query("select b from Booking b " +
            "where b.booker.id = :bookerId " +
            "and b.end > :currentDate " +
            "order by b.end desc")
    List<Booking> findByBookerFutureBookings(@Param("bookerId") Long bookerId, @Param("currentDate") LocalDateTime currentDate);

    List<Booking> findByBooker_IdAndStatusOrderByEndDesc(Long bookerId, Booking.Status status);

    // Получение двух последних бронирований для владельца вещи
    @Query("select b from Booking b " +
            "where b.item.owner.id = :ownerId " +
            "and b.item.id = :itemId")
    Page<Booking> getLastBookings(@Param("ownerId") Long ownerId, @Param("itemId") Long itemId, Pageable pageable);
}