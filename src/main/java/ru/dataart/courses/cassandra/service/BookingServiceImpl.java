package ru.dataart.courses.cassandra.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.dataart.courses.cassandra.repository.entities.booking.Booking;
import ru.dataart.courses.cassandra.repository.entities.booking.BookingDetail;
import ru.dataart.courses.cassandra.repository.entities.guest.Guest;
import ru.dataart.courses.cassandra.repository.entities.hotel.City;
import ru.dataart.courses.cassandra.repository.entities.hotel.Hotel;
import ru.dataart.courses.cassandra.repository.entities.hotel.Room;
import ru.dataart.courses.cassandra.repository.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);
    private BookingDetailRepository bookingDetailRepository;
    private BookingHotelDetailRepository bookingHotelDetailRepository;
    private SaveRepository saveRepository;
    private HotelRepository hotelRepository;
    private CityRepository cityRepository;
    private GuestRepository guestRepository;

    public BookingServiceImpl(@Autowired BookingDetailRepository bookingDetailRepository,
                              @Autowired BookingHotelDetailRepository bookingHotelDetailRepository,
                              @Autowired SaveRepository saveRepository,
                              @Autowired HotelRepository hotelRepository,
                              @Autowired GuestRepository guestRepository) {
        this.bookingDetailRepository = bookingDetailRepository;
        this.bookingHotelDetailRepository = bookingHotelDetailRepository;
        this.saveRepository = saveRepository;
        this.hotelRepository = hotelRepository;
    }

    @Override
    public boolean saveHotel(Hotel hotel) {
        try {
            saveRepository.saveHotel(hotel);
            logger.info("{} has been successfully saved", hotel.getHotelKey());
        } catch (Exception e) {
            logger.error("{} hasn't been saved", hotel.getHotelKey());
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean saveCity(City city) {
        try {
            saveRepository.saveCity(city);
            logger.info("{} has been successfully saved", city.getCityKey());
        } catch (Exception e) {
            logger.error("{} hasn't been saved", city.getCityKey());
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean saveRoom(Room room) {
        try {
            saveRepository.saveRoom(room);
            logger.info("{} has been successfully saved", room.getRoomKey());
        } catch (Exception e) {
            logger.error("{} hasn't been saved", room.getRoomKey());
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean saveGuest(Guest guest) {
        try {
            saveRepository.saveGuest(guest);
            logger.info("{} has been successfully saved", guest.getGuestKey());
        } catch (Exception e) {
            logger.error("{} hasn't been saved", guest.getGuestKey());
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean saveBooking(Booking booking) {
        try {
            saveRepository.saveBooking(booking);
            logger.info("{} has been successfully saved", booking.getBookingKey());
        } catch (Exception e) {
            logger.error("{} hasn't been saved", booking.getBookingKey());
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }


    @Override
    public List<Integer> getFreeRooms(String hotelName, String city, LocalDateTime startReserveTime, LocalDateTime endReserveTime) {
        try {
            Hotel hotel = hotelRepository.findHotelByCityAndName(hotelName, city);
            List<Integer> details = bookingHotelDetailRepository.findBookedRooms(city, hotelName, Timestamp.valueOf(startReserveTime), Timestamp.valueOf(endReserveTime))
                    .map(BookingDetail::getRoomNumber).collect(Collectors.toList());
            logger.info("BookingDetails has been successfully queried");
            return hotel.getRooms().stream().filter(roomNumber -> !details.contains(roomNumber)).collect(Collectors.toList());
        } catch (Exception e) {
            logger.info("BookingDetails hasn't been successfully queried");
            logger.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public List<BookingDetail> getReservedRooms(UUID guestId, LocalDateTime dt) {
        return bookingDetailRepository.findAllByGuestAndDate(guestId, Date.valueOf(dt.toLocalDate())).collect(Collectors.toList());
    }

    @Override
    public List<City> findAllByCityName(String cityName) {
        return cityRepository.findAllByCityName(cityName);
    }

    public Guest findGuestByName(String name){
        return guestRepository.findOneByGuestName(name);
    }

}
