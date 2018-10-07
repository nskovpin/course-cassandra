package ru.dataart.courses.cassandra.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.dataart.courses.cassandra.repository.entities.booking.BookingDetail;
import ru.dataart.courses.cassandra.repository.entities.booking.BookingHotelDetail;
import ru.dataart.courses.cassandra.repository.entities.guest.Guest;
import ru.dataart.courses.cassandra.repository.entities.hotel.Hotel;
import ru.dataart.courses.cassandra.repository.entities.hotel.Room;
import ru.dataart.courses.cassandra.repository.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);
    private BookingDetailRepository bookingDetailRepository;
    private BookingHotelDetailRepository bookingHotelDetailRepository;
    private SaveRepository saveRepository;
    private HotelRepository hotelRepository;
    private GuestRepository guestRepository;
    private RoomRepository roomRepository;

    public BookingServiceImpl(@Autowired BookingDetailRepository bookingDetailRepository,
                              @Autowired BookingHotelDetailRepository bookingHotelDetailRepository,
                              @Autowired SaveRepository saveRepository,
                              @Autowired HotelRepository hotelRepository,
                              @Autowired GuestRepository guestRepository,
                              @Autowired RoomRepository roomRepository) {
        this.bookingDetailRepository = bookingDetailRepository;
        this.bookingHotelDetailRepository = bookingHotelDetailRepository;
        this.saveRepository = saveRepository;
        this.hotelRepository = hotelRepository;
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
    }

    @Async
    @Override
    public CompletableFuture<Boolean> saveHotel(Hotel hotel) {
        try {
            saveRepository.saveHotel(hotel);
            logger.info("{} has been successfully saved", hotel.getHotelKey());
        } catch (Exception e) {
            logger.error("{} hasn't been saved", hotel.getHotelKey());
            logger.error(e.getMessage());
            return CompletableFuture.completedFuture(false);
        }
        return CompletableFuture.completedFuture(true);
    }

    @Async
    @Override
    public CompletableFuture<Boolean> saveRoom(Room room, Hotel hotel) {
        try {
            saveRepository.saveRoom(room, hotel);
            logger.info("{} has been successfully saved", room.getRoomKey());
        } catch (Exception e) {
            logger.error("{} hasn't been saved", room.getRoomKey());
            logger.error(e.getMessage());
            return CompletableFuture.completedFuture(false);
        }
        return CompletableFuture.completedFuture(true);
    }

    @Async
    @Override
    public CompletableFuture<Boolean> saveGuest(Guest guest) {
        try {
            saveRepository.saveGuest(guest);
            logger.info("{} has been successfully saved", guest.getGuestKey());
        } catch (Exception e) {
            logger.error("{} hasn't been saved", guest.getGuestKey());
            logger.error(e.getMessage());
            return CompletableFuture.completedFuture(false);
        }
        return CompletableFuture.completedFuture(true);
    }

    @Async
    @Override
    public CompletableFuture<Boolean> saveBookingDetails(BookingDetail bookingDetail) {
        try {
            saveRepository.saveBookingDetail(bookingDetail);
            logger.info("{} has been successfully saved", bookingDetail.getBookingDetailKey());
        } catch (Exception e) {
            logger.error("{} hasn't been saved", bookingDetail.getBookingDetailKey());
            logger.error(e.getMessage());
            return CompletableFuture.completedFuture(false);
        }
        return CompletableFuture.completedFuture(true);
    }

    @Async
    @Override
    public CompletableFuture<Boolean> saveBookingHotelDetails(BookingHotelDetail bookingHotelDetail) {
        try {
            saveRepository.saveBookingHotelDetail(bookingHotelDetail);
            logger.info("{} has been successfully saved", bookingHotelDetail.getBookingHotelDetailKey());
        } catch (Exception e) {
            logger.error("{} hasn't been saved", bookingHotelDetail.getBookingHotelDetailKey());
            logger.error(e.getMessage());
            return CompletableFuture.completedFuture(false);
        }
        return CompletableFuture.completedFuture(true);
    }


    @Override
    public List<Integer> getFreeRooms(String hotelName, String city, LocalDateTime startReserveTime, LocalDateTime endReserveTime) {
        try {
            Hotel hotel = hotelRepository.findHotelByCityAndName(hotelName, city);
            if(Objects.isNull(hotel)){
                throw new RuntimeException("Sorry, but this hotel doesn't exist");
            }
            List<BookingHotelDetail> select = bookingHotelDetailRepository.findBookedRooms(hotel.getId(), Timestamp.valueOf(startReserveTime), Timestamp.valueOf(endReserveTime));
            List<Integer> details = select
                    .stream()
                    .map(x -> x.getBookingHotelDetailKey().getRoomNumber()).collect(Collectors.toList());
            logger.info("BookingDetails has been successfully queried");
            return hotel.getRooms().stream().filter(roomNumber -> !details.contains(roomNumber)).collect(Collectors.toList());
        } catch (Exception e) {
            logger.info("BookingDetails hasn't been successfully queried");
            logger.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public List<BookingDetail> getReservedRooms(UUID guestId, LocalDate dt) {
        return bookingDetailRepository.findAllByGuestAndDate(guestId, dt).collect(Collectors.toList());
    }

    @Override
    public List<Hotel> findAllByCityName(String cityName) {
        return hotelRepository.findHotelsByCity(cityName);
    }

    @Async
    @Override
    public CompletableFuture<List<Hotel>> findAllByCityNameComputable(String cityName) {
        logger.info("Finding hotels by city");
        return CompletableFuture.completedFuture(hotelRepository.findHotelsByCity(cityName));
    }

    @Override
    public Hotel findByCityAndHotel(String cityName, String hotelName) {
        logger.info("Finding hotel by city and name");
        return  hotelRepository.findHotelByCityAndName(hotelName, cityName);
    }

    public Guest findGuestByName(String name){
        return guestRepository.findOneByGuestName(name);
    }

    @Override
    public Room findRoom(Integer roomNumber, UUID hotelId) {
        return roomRepository.findOneByRoomAndHotel(roomNumber, hotelId);
    }

}
