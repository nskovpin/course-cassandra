package ru.dataart.courses.cassandra.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dataart.courses.cassandra.config.SelfChecked;
import ru.dataart.courses.cassandra.repository.entities.booking.Booking;
import ru.dataart.courses.cassandra.repository.entities.guest.Guest;
import ru.dataart.courses.cassandra.repository.entities.hotel.Hotel;
import ru.dataart.courses.cassandra.repository.entities.hotel.Room;
import ru.dataart.courses.cassandra.service.BookingService;
import ru.dataart.courses.cassandra.web.entities.*;

import javax.validation.constraints.NotNull;
import java.io.ObjectStreamClass;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api")
public class CassandraRestApi {
    private static final Logger logger = LoggerFactory.getLogger(CassandraRestApi.class);

    private BookingService bookingService;

    public CassandraRestApi(@Autowired BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public String test() {
        return "Test";
    }

    @RequestMapping(path = "/get/city", method = RequestMethod.GET)
    @SelfChecked
    public ResponseEntity<List<CityResponse>> getHotelsByCity(@RequestParam("name") @NotNull String name) {
        return new ResponseEntity<>(bookingService.findAllByCityName(name)
                .stream()
                .map(x -> new CityResponse(x.getCityKey().getCityName(), x.getCityKey().getHotelName()))
                .collect(Collectors.toList()),HttpStatus.FOUND);
    }

    @RequestMapping(path = "/add/hotel", method = RequestMethod.POST)
    public ResponseEntity<Hotel> addNewHotel(@RequestBody @NotNull HotelRequest hotel) {
        Objects.requireNonNull(hotel);
        Hotel hotelDb = new Hotel();
        hotelDb.getHotelKey().setCityName(hotel.getCity());
        hotelDb.getHotelKey().setHotelName(hotel.getHotel());
        hotelDb.setAddress(hotel.getAddress());
        hotelDb.setRooms(hotel.getRooms());
        bookingService.saveHotel(hotelDb);
        return new ResponseEntity<>(hotelDb, HttpStatus.CREATED);
    }

    @RequestMapping(path = "/add/hotel", method = RequestMethod.POST)
    public ResponseEntity<Room> addNewRoom(@RequestBody @NotNull RoomRequest room) {
        Objects.requireNonNull(room);
        Room roomDb = new Room();
        roomDb.getRoomKey().setCity(room.getCity());
        roomDb.getRoomKey().setHotel(room.getHotel());
        roomDb.getRoomKey().setRoomNumber(room.getRoomNumber());
        bookingService.saveRoom(roomDb);
        return new ResponseEntity<>(roomDb, HttpStatus.CREATED);
    }

    @RequestMapping(path = "/add/guest", method = RequestMethod.POST)
    public ResponseEntity<Guest> addNewGuest(@RequestBody @NotNull GuestRequest guest) {
        Objects.requireNonNull(guest);
        Guest guestDb = new Guest();
        guestDb.getGuestKey().setGuestName(guest.getName());
        bookingService.saveGuest(guestDb);
        return new ResponseEntity<>(guestDb, HttpStatus.CREATED);
    }

    @RequestMapping(path = "/add/booking", method = RequestMethod.POST)
    public ResponseEntity<?> addNewGuest(@RequestBody @NotNull BookingRequest bookingRequest) {
        Objects.requireNonNull(bookingRequest);
        Guest guestDb = bookingService.findGuestByName(bookingRequest.getGuestName());
        if(Objects.isNull(guestDb)){
            guestDb = new Guest();
            guestDb.getGuestKey().setGuestName(bookingRequest.getGuestName());
            bookingService.saveGuest(guestDb);
        }
        Booking bookingDb = new Booking();
        bookingDb.setGuestId(guestDb.getGuestKey().getId());
        bookingDb.setComment(bookingRequest.getComment());
        bookingService.saveBooking(bookingDb);


        bookingService.saveGuest(guestDb);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/get/freerooms", method = RequestMethod.GET)
    public ResponseEntity<List<Integer>> getHotelFreeRooms(@RequestParam @NotNull String hotel,
                                               @RequestParam @NotNull String city,
                                               @RequestParam @NotNull LocalDateTime startReverse,
                                               @RequestParam @NotNull LocalDateTime endReverse) {
       return new ResponseEntity<>(bookingService.getFreeRooms(hotel, city, startReverse, endReverse),
               HttpStatus.FOUND);
    }

    @RequestMapping(path = "/get/roombyguest", method = RequestMethod.GET)
    public ResponseEntity<List<BookedResponse>> getShit(@RequestParam String userId,
                                                        @RequestParam LocalDateTime date){
        UUID id = UUID.fromString(userId);
        return new ResponseEntity<>(bookingService.getReservedRooms(id, date)
                .stream()
                .map(x -> new BookedResponse())
                .collect(Collectors.toList()), HttpStatus.FOUND);
    }

}
