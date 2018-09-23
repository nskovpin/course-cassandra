package ru.dataart.courses.cassandra.web;

import com.datastax.driver.core.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dataart.courses.cassandra.config.ApiChecked;
import ru.dataart.courses.cassandra.repository.SaveRepository;
import ru.dataart.courses.cassandra.repository.entities.booking.Booking;
import ru.dataart.courses.cassandra.repository.entities.booking.BookingDetail;
import ru.dataart.courses.cassandra.repository.entities.booking.BookingDetailKey;
import ru.dataart.courses.cassandra.repository.entities.booking.BookingHotelDetail;
import ru.dataart.courses.cassandra.repository.entities.guest.Guest;
import ru.dataart.courses.cassandra.repository.entities.hotel.Hotel;
import ru.dataart.courses.cassandra.repository.entities.hotel.Room;
import ru.dataart.courses.cassandra.service.BookingService;
import ru.dataart.courses.cassandra.web.entities.*;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

import static ru.dataart.courses.cassandra.repository.SaveRepository.BeginEnd.BEGIN;

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
    @ApiChecked
    public ResponseEntity<List<CityResponse>> getHotelsByCity(@RequestParam("name") @NotNull String name) {
        return new ResponseEntity<>(bookingService.findAllByCityName(name)
                .stream()
                .map(x -> new CityResponse(x.getCityKey().getCityName(), x.getCityKey().getHotelName()))
                .collect(Collectors.toList()), HttpStatus.FOUND);
    }

    /**
     * localhost:8080/api/add/hotel
     * {
     "hotel": "hostel",
     "rooms": [1,2,3],
     "city": "voronezh",
     "address": "moskovskiy pr"
     }
     */
    @RequestMapping(path = "/add/hotel", method = RequestMethod.POST, consumes = "application/json")
    @ApiChecked
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

    /**
     *
     localhost:8080/api/add/room
     {
     "hotel": "hostel",
     "roomNumber": 14,
     "city": "voronezh"
     }
     */
    @RequestMapping(path = "/add/room", method = RequestMethod.POST, consumes = "application/json")
    @ApiChecked
    public ResponseEntity<Room> addNewRoom(@RequestBody @NotNull RoomRequest room) {
        Objects.requireNonNull(room);
        Objects.requireNonNull(room.getRoomNumber());
        Room roomDb = new Room();
        roomDb.getRoomKey().setCity(room.getCity());
        roomDb.getRoomKey().setHotel(room.getHotel());
        roomDb.getRoomKey().setRoomNumber(room.getRoomNumber());
        bookingService.saveRoom(roomDb);
        return new ResponseEntity<>(roomDb, HttpStatus.CREATED);
    }

    /**
     * localhost:8080/api/add/guest
     * {
     	"name": "Nikolay S"
     }
     */
    @RequestMapping(path = "/add/guest", method = RequestMethod.POST, consumes = "application/json")
    @ApiChecked
    public ResponseEntity<Guest> addNewGuest(@RequestBody @NotNull GuestRequest guest) {
        Objects.requireNonNull(guest);
        Guest guestDb = new Guest();
        guestDb.getGuestKey().setGuestName(guest.getName());
        bookingService.saveGuest(guestDb);
        return new ResponseEntity<>(guestDb, HttpStatus.CREATED);
    }

    /**
     *
     localhost:8080/api/add/booking
     {
     	 "guestName" : "Nikolay S",
                  "comment" : "random comment",
                  "start": "2018-08-23",
                  "end": "2018-08-26",
                  "roomNumber": 2,
                  "hotel": "mariya",
                  "city": "voronezh"
     }
     */
    @RequestMapping(path = "/add/booking", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> addNewBooking(@RequestBody @NotNull BookingRequest bookingRequest) {
        Objects.requireNonNull(bookingRequest);
        Objects.requireNonNull(bookingRequest.getStart());
        Objects.requireNonNull(bookingRequest.getEnd());
        Guest guestDb = bookingService.findGuestByName(bookingRequest.getGuestName());
        Room room = bookingService.findRoom(bookingRequest.getRoomNumber(), bookingRequest.getHotel(), bookingRequest.getCity());
        if(room == null){
            return new ResponseEntity<>("Sorry, this room doesn't exist", HttpStatus.NOT_FOUND);
        }
        if(Objects.isNull(guestDb)){
            guestDb = new Guest();
            guestDb.getGuestKey().setGuestName(bookingRequest.getGuestName());
            bookingService.saveGuest(guestDb);
        }
        List<Integer> rooms = bookingService.getFreeRooms(bookingRequest.getHotel(), bookingRequest.getCity(),
                bookingRequest.getStart().toLocalDateTime(), bookingRequest.getEnd().toLocalDateTime());
        if(!rooms.contains(bookingRequest.getRoomNumber())){
            return new ResponseEntity<>("Sorry, this room is booked", HttpStatus.NOT_FOUND);
        }
        Booking bookingDb = new Booking();
        bookingDb.setGuestId(guestDb.getGuestKey().getId());
        bookingDb.setComment(bookingRequest.getComment());
        bookingService.saveBooking(bookingDb);
        //loop
        List<BookingDetail> bookingsByDays = splitByDays(bookingRequest, guestDb.getGuestKey().getId(), bookingDb.getBookingKey().getId());
        bookingsByDays.forEach(x -> bookingService.saveBookingDetails(x));

        BookingHotelDetail startEntity = createBookingHotelDetails(bookingRequest, bookingDb.getBookingKey().getId(), SaveRepository.BeginEnd.BEGIN);
        BookingHotelDetail endEntity = createBookingHotelDetails(bookingRequest, bookingDb.getBookingKey().getId(), SaveRepository.BeginEnd.END);
        bookingService.saveBookingHotelDetails(startEntity);
        bookingService.saveBookingHotelDetails(endEntity);
        return new ResponseEntity<>(bookingDb, HttpStatus.CREATED);
    }

    //localhost:8080/api/get/freerooms?hotel=mariya&city=voronezh&startReserve=2018-07-07T11:11:11&endReserve=2018-08-23T11:11:11
    @RequestMapping(path = "/get/freerooms", method = RequestMethod.GET)
    public ResponseEntity<List<Integer>> getHotelFreeRooms(@RequestParam @NotNull String hotel,
                                               @RequestParam @NotNull String city,
                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @NotNull LocalDateTime startReserve,
                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @NotNull LocalDateTime endReserve) {
       return new ResponseEntity<>(bookingService.getFreeRooms(hotel, city, startReserve, endReserve),
               HttpStatus.FOUND);
    }

    //
    @RequestMapping(path = "/get/roombyguest", method = RequestMethod.GET)
    public ResponseEntity<?> getRoomByGuest(@RequestParam String guestName,
                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.time.LocalDate date){
        Guest guest = bookingService.findGuestByName(guestName);
        if(guest == null){
            return new ResponseEntity<>("We don't know this user", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(bookingService.getReservedRooms(guest.getGuestKey().getId(), date)
                .stream()
                .map(x -> {
                    BookedResponse resp = new BookedResponse();
                    resp.setCity(x.getCity());
                    resp.setHotel(x.getHotel());
                    resp.setRoomNumber(x.getRoomNumber());
                    resp.setStart(LocalDateTime.ofInstant(x.getStart().toInstant(), ZoneOffset.UTC).toLocalDate());
                    resp.setEnd(LocalDateTime.ofInstant(x.getEnd().toInstant(), ZoneOffset.UTC).toLocalDate());
                    return resp;
                })
                .collect(Collectors.toList()), HttpStatus.FOUND);
    }

    private List<BookingDetail> splitByDays(BookingRequest bookingRequest, UUID guestId, UUID bookingId){
        LocalDateTime start = bookingRequest.getStart().toLocalDateTime();
        LocalDateTime end = bookingRequest.getEnd().toLocalDateTime();
        List<BookingDetail> days = new ArrayList<>();
        while (start.isBefore(end)){
            LocalDateTime day = start;
            BookingDetail bookingDetail = new BookingDetail();
            bookingDetail.setBookingDetailKey(new BookingDetailKey(LocalDate.fromYearMonthDay(day.toLocalDate().getYear(),
                    day.toLocalDate().getMonth().getValue(),
                    day.toLocalDate().getDayOfMonth()), guestId));
            bookingDetail.setBookingId(bookingId);
            bookingDetail.setCity(bookingRequest.getCity());
            bookingDetail.setStart(bookingRequest.getStart());
            bookingDetail.setEnd(bookingRequest.getEnd());
            bookingDetail.setHotel(bookingRequest.getHotel());
            bookingDetail.setRoomNumber(bookingRequest.getRoomNumber());
            days.add(bookingDetail);
            start = day.plusDays(1);
        }
        return days;
    }

    private BookingHotelDetail createBookingHotelDetails(BookingRequest bookingRequest, UUID bookingId, SaveRepository.BeginEnd beginEnd){
        BookingHotelDetail bookingHotelDetail = new BookingHotelDetail();
        bookingHotelDetail.getBookingHotelDetailKey().setHotel(bookingRequest.getHotel());
        bookingHotelDetail.getBookingHotelDetailKey().setCity(bookingRequest.getCity());
        bookingHotelDetail.getBookingHotelDetailKey().setBeginEnd(beginEnd.getName());
        switch (beginEnd){
            case BEGIN:{
                bookingHotelDetail.getBookingHotelDetailKey().setEventDate(bookingRequest.getStart());
                break;
            }
            case END:{
                bookingHotelDetail.getBookingHotelDetailKey().setEventDate(bookingRequest.getEnd());
                break;
            }
        }
        bookingHotelDetail.getBookingHotelDetailKey().setRoomNumber(bookingRequest.getRoomNumber());
        bookingHotelDetail.setBookingId(bookingId);
        return bookingHotelDetail;
    }

}
