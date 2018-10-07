package ru.dataart.courses.cassandra.service;

import ru.dataart.courses.cassandra.repository.entities.booking.BookingDetail;
import ru.dataart.courses.cassandra.repository.entities.booking.BookingHotelDetail;
import ru.dataart.courses.cassandra.repository.entities.guest.Guest;
import ru.dataart.courses.cassandra.repository.entities.hotel.Hotel;
import ru.dataart.courses.cassandra.repository.entities.hotel.Room;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface BookingService {

    CompletableFuture<Boolean> saveHotel(Hotel hotel);

    CompletableFuture<Boolean> saveRoom(Room room, Hotel hotel);

    CompletableFuture<Boolean> saveGuest(Guest guest);

    CompletableFuture<Boolean> saveBookingDetails(BookingDetail bookingDetail);

    CompletableFuture<Boolean> saveBookingHotelDetails(BookingHotelDetail bookingHotelDetail);

    List<Integer> getFreeRooms(String hotelName, String city, LocalDateTime startReserveTime, LocalDateTime endReserveTime);

    List<BookingDetail> getReservedRooms(UUID guestId, LocalDate dt);

    List<Hotel> findAllByCityName(String cityName);

    CompletableFuture<List<Hotel>> findAllByCityNameComputable(String cityName);

    Hotel findByCityAndHotel(String cityName, String hotelName);

    Guest findGuestByName(String name);

    Room findRoom(Integer roomNumber, UUID hotelId);
}
