package ru.dataart.courses.cassandra.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dataart.courses.cassandra.config.SelfChecked;
import ru.dataart.courses.cassandra.repository.entities.guest.Guest;
import ru.dataart.courses.cassandra.repository.entities.hotel.Hotel;
import ru.dataart.courses.cassandra.service.BookingService;
import ru.dataart.courses.cassandra.web.entities.BookedResponse;
import ru.dataart.courses.cassandra.web.entities.CityResponse;
import ru.dataart.courses.cassandra.web.entities.GuestRequest;
import ru.dataart.courses.cassandra.web.entities.HotelRequest;

import javax.validation.constraints.NotNull;
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
    public List<ResponseEntity<CityResponse>> getAllCities(@RequestParam("name") @NotNull String name) {
        return bookingService.findAllByCityName(name)
                .stream()
                .map(x -> new CityResponse(x.getCityKey().getCityName(), x.getCityKey().getHotelName()))
                .map(x -> new ResponseEntity<>(x, HttpStatus.FOUND))
                .collect(Collectors.toList());
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

    @RequestMapping(path = "/add/guest", method = RequestMethod.POST)
    public ResponseEntity<?> addNewGuest(@RequestBody @NotNull GuestRequest guest) {
        Guest guestDb = new Guest();
        guestDb.getGuestKey().setGuestName(guest.getName());
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
    public List<ResponseEntity<BookedResponse>> getShit(@RequestParam String userId,
                                                        @RequestParam LocalDateTime date){
        UUID id = UUID.fromString(userId);
        return bookingService.getReservedRooms(id, date)
                .stream()
                .map(x -> new BookedResponse())
                .map(x -> new ResponseEntity<BookedResponse>(x, HttpStatus.FOUND))
                .collect(Collectors.toList());
    }

}
