package ru.dataart.courses.cassandra.service;

import ru.dataart.courses.cassandra.repository.SaveRepository;
import ru.dataart.courses.cassandra.repository.entities.booking.Booking;
import ru.dataart.courses.cassandra.repository.entities.booking.BookingDetail;
import ru.dataart.courses.cassandra.repository.entities.booking.BookingHotelDetail;
import ru.dataart.courses.cassandra.repository.entities.guest.Guest;
import ru.dataart.courses.cassandra.repository.entities.hotel.City;
import ru.dataart.courses.cassandra.repository.entities.hotel.Hotel;
import ru.dataart.courses.cassandra.repository.entities.hotel.Room;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface BookingService {

    boolean saveHotel(Hotel hotel);

    boolean saveCity(City city);

    boolean saveRoom(Room room);

    boolean saveGuest(Guest guest);

    boolean saveBooking(Booking booking);

    boolean saveBookingDetails(BookingDetail bookingDetail);

    boolean saveBookingHotelDetails(BookingHotelDetail bookingHotelDetail);

    List<Integer> getFreeRooms(String hotelName, String city, LocalDateTime startReserveTime, LocalDateTime endReserveTime);

    List<BookingDetail> getReservedRooms(UUID guestId, LocalDateTime dt);

    List<City> findAllByCityName(String cityName);

    Guest findGuestByName(String name);
}
