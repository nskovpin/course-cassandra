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
import ru.dataart.courses.cassandra.repository.entities.booking.BookingDetail;
import ru.dataart.courses.cassandra.repository.entities.booking.BookingDetailKey;
import ru.dataart.courses.cassandra.repository.entities.booking.BookingHotelDetail;
import ru.dataart.courses.cassandra.repository.entities.guest.Guest;
import ru.dataart.courses.cassandra.repository.entities.hotel.Hotel;
import ru.dataart.courses.cassandra.repository.entities.hotel.Room;
import ru.dataart.courses.cassandra.service.BookingService;
import ru.dataart.courses.cassandra.web.entities.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.CompletableFuture;
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
    @ApiChecked
    public CompletableFuture<ResponseEntity<List<HotelResponse>>> getHotelsByCity(@RequestParam("name") @NotNull String name) {
        return CompletableFuture.supplyAsync(() ->{
            List<HotelResponse> list = bookingService.findAllByCityName(name).stream()
                    .map(y -> new HotelResponse(y.getHotelKey().getCity(), y.getHotelKey().getHotel()))
                    .collect(Collectors.toList());
         return new ResponseEntity<>(list, HttpStatus.FOUND);
        });

    }

    /**
     * localhost:8080/api/add/hotel
     * {
     * "hotel": "hostel",
     * "rooms": [1,2,3],
     * "city": "voronezh",
     * "address": "moskovskiy pr"
     * }
     */
    @RequestMapping(path = "/add/hotel", method = RequestMethod.POST, consumes = "application/json")
    @ApiChecked
    public ResponseEntity<Hotel> addNewHotel(@RequestBody @NotNull HotelRequest hotel) {
        Objects.requireNonNull(hotel);
        Hotel hotelDb = new Hotel();
        hotelDb.getHotelKey().setCity(hotel.getCity());
        hotelDb.getHotelKey().setHotel(hotel.getHotel());
        hotelDb.setAddress(hotel.getAddress());
        hotelDb.setRooms(hotel.getRooms());
        bookingService.saveHotel(hotelDb);
        return new ResponseEntity<>(hotelDb, HttpStatus.CREATED);
    }

    /**
     * localhost:8080/api/add/room
     * {
     * "hotel": "hostel",
     * "roomNumber": 14,
     * "city": "voronezh"
     * }
     */
    @RequestMapping(path = "/add/room", method = RequestMethod.POST, consumes = "application/json")
    @ApiChecked
    public ResponseEntity<Room> addNewRoom(@RequestBody @NotNull RoomRequest room) {
        Objects.requireNonNull(room);
        Objects.requireNonNull(room.getRoomNumber());
        Room roomDb = new Room();
        roomDb.getRoomKey().setRoomNumber(room.getRoomNumber());
        CompletableFuture.supplyAsync(() -> bookingService.findByCityAndHotel(room.getCity(), room.getHotel()))
                .thenApplyAsync(x -> {
                    if (Objects.isNull(x)) {
                        x = new Hotel();
                        x.getHotelKey().setCity(room.getCity());
                        x.getHotelKey().setHotel(room.getHotel());
                        x.getRooms().add(room.getRoomNumber());
                        bookingService.saveHotel(x);
                    }
                    return x;
                })
                .thenAcceptAsync(x -> {
                    roomDb.getRoomKey().setHotelId(x.getId());
                    bookingService.saveRoom(roomDb, x);
                });
        return new ResponseEntity<>(roomDb, HttpStatus.CREATED);
    }

    /**
     * localhost:8080/api/add/guest
     * {
     * "name": "Nikolay S"
     * }
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
     * localhost:8080/api/add/booking
     * {
     * "guestName" : "Nikolay S",
     * "comment" : "random comment",
     * "start": "2018-08-23",
     * "end": "2018-08-26",
     * "roomNumber": 2,
     * "hotel": "mariya",
     * "city": "voronezh"
     * }
     */
    @RequestMapping(path = "/add/booking", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<StatusResponse> addNewBooking(@RequestBody @NotNull BookingRequest bookingRequest) {
        Objects.requireNonNull(bookingRequest);
        Objects.requireNonNull(bookingRequest.getStart());
        Objects.requireNonNull(bookingRequest.getEnd());
        CompletableFuture<Void> res = CompletableFuture.supplyAsync(() -> {
            Guest guestDb = bookingService.findGuestByName(bookingRequest.getGuestName());
            if (Objects.isNull(guestDb)) {
                guestDb = new Guest();
                guestDb.getGuestKey().setGuestName(bookingRequest.getGuestName());
                bookingService.saveGuest(guestDb);
            }
            return guestDb;
        }).thenAcceptBothAsync(CompletableFuture.supplyAsync(() -> {
                    List<Integer> rooms = bookingService.getFreeRooms(bookingRequest.getHotel(), bookingRequest.getCity(),
                            bookingRequest.getStart().toLocalDateTime(), bookingRequest.getEnd().toLocalDateTime());
                    if (rooms.contains(bookingRequest.getRoomNumber())) {
                        System.out.println("Room is available");
                        return bookingService.findByCityAndHotel(bookingRequest.getCity(), bookingRequest.getHotel());
                    } else {
                        System.out.println("Room is not available");
                        return null;
                    }
                }), (g, h) -> {
                    if (!Objects.isNull(h)) {
                        List<BookingDetail> bookingsByDays = splitByDays(
                                bookingRequest,
                                g.getGuestKey().getId(),
                                h.getId()
                        );
                        bookingsByDays.forEach(x -> bookingService.saveBookingDetails(x));
                        BookingHotelDetail startEntity = createBookingHotelDetails(
                                bookingRequest,
                                h.getId(),
                                SaveRepository.BeginEnd.BEGIN
                        );
                        BookingHotelDetail endEntity = createBookingHotelDetails(
                                bookingRequest,
                                h.getId(),
                                SaveRepository.BeginEnd.END
                        );
                        bookingService.saveBookingHotelDetails(startEntity);
                        bookingService.saveBookingHotelDetails(endEntity);
                    }

                }
        );
        return new ResponseEntity<>(new StatusResponse("Check the status of booking"), HttpStatus.CREATED);
    }

    //localhost:8080/api/get/freerooms?hotel=mariya&city=voronezh&startReserve=2018-07-07T11:11:11&endReserve=2018-08-23T11:11:11
    @RequestMapping(path = "/get/freerooms", method = RequestMethod.GET)
    public CompletableFuture<ResponseEntity<List<Integer>>> getHotelFreeRooms(
            @RequestParam @NotNull String hotel,
            @RequestParam @NotNull String city,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @NotNull LocalDateTime startReserve,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @NotNull LocalDateTime endReserve
    ) {
        return CompletableFuture.supplyAsync(() ->
                new ResponseEntity<>(bookingService.getFreeRooms(hotel, city, startReserve, endReserve),
                        HttpStatus.FOUND));
    }

    //localhost:8080/api/get/roombyguest?guestName=Nikolay S&date=2018-08-25
    @RequestMapping(path = "/get/roombyguest", method = RequestMethod.GET)
    public CompletableFuture<ResponseEntity<?>> getRoomByGuest(
            @RequestParam String guestName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.time.LocalDate date
    ) {
        return CompletableFuture.supplyAsync(() -> bookingService.findGuestByName(guestName))
                .thenApplyAsync(guest1 -> {
                    if(Objects.isNull(guest1)){
                        return new ResponseEntity<>(Collections.EMPTY_LIST, HttpStatus.NOT_FOUND);
                    }else {
                        return new ResponseEntity<>(bookingService.getReservedRooms(guest1.getGuestKey().getId(), date)
                                .stream()
                                .map(x -> {
                                    BookedResponse resp = new BookedResponse();
                                    resp.setHotelId(x.getHotelId()); //returning only id, because we can't just query hotel by id, we need additional table for that (that i was asked to deleted)
                                    resp.setRoomNumber(x.getRoomNumber());
                                    resp.setStart(LocalDateTime.ofInstant(x.getStart().toInstant(), ZoneOffset.UTC).toLocalDate());
                                    resp.setEnd(LocalDateTime.ofInstant(x.getEnd().toInstant(), ZoneOffset.UTC).toLocalDate());
                                    return resp;
                                })
                                .collect(Collectors.toList()), HttpStatus.FOUND);
                    }
                });
    }

    private List<BookingDetail> splitByDays(BookingRequest bookingRequest, UUID guestId, UUID hotelId) {
        LocalDateTime start = bookingRequest.getStart().toLocalDateTime();
        LocalDateTime end = bookingRequest.getEnd().toLocalDateTime();
        List<BookingDetail> days = new ArrayList<>();
        while (start.isBefore(end)) {
            LocalDateTime day = start;
            BookingDetail bookingDetail = new BookingDetail();
            bookingDetail.setBookingDetailKey(new BookingDetailKey(LocalDate.fromYearMonthDay(day.toLocalDate().getYear(),
                    day.toLocalDate().getMonth().getValue(),
                    day.toLocalDate().getDayOfMonth()), guestId));
            bookingDetail.setHotelId(hotelId);
            bookingDetail.setStart(bookingRequest.getStart());
            bookingDetail.setEnd(bookingRequest.getEnd());
            bookingDetail.setRoomNumber(bookingRequest.getRoomNumber());
            days.add(bookingDetail);
            start = day.plusDays(1);
        }
        return days;
    }

    private BookingHotelDetail createBookingHotelDetails(BookingRequest bookingRequest, UUID hotelId, SaveRepository.BeginEnd beginEnd) {
        BookingHotelDetail bookingHotelDetail = new BookingHotelDetail();
        bookingHotelDetail.getBookingHotelDetailKey().setHotelId(hotelId);
        bookingHotelDetail.getBookingHotelDetailKey().setBeginEnd(beginEnd.getName());
        switch (beginEnd) {
            case BEGIN: {
                bookingHotelDetail.getBookingHotelDetailKey().setEventDate(bookingRequest.getStart());
                break;
            }
            case END: {
                bookingHotelDetail.getBookingHotelDetailKey().setEventDate(bookingRequest.getEnd());
                break;
            }
        }
        bookingHotelDetail.getBookingHotelDetailKey().setRoomNumber(bookingRequest.getRoomNumber());
        return bookingHotelDetail;
    }

}
