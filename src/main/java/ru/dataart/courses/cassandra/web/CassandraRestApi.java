package ru.dataart.courses.cassandra.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dataart.courses.cassandra.repository.entities.guest.Guest;
import ru.dataart.courses.cassandra.repository.entities.hotel.Hotel;
import ru.dataart.courses.cassandra.service.BookingService;
import ru.dataart.courses.cassandra.web.response.BookedResponse;
import ru.dataart.courses.cassandra.web.response.CityResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api")
public class CassandraRestApi {

    private BookingService bookingService;

    public CassandraRestApi(@Autowired BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public String test() {
        return "Test";
    }

    @RequestMapping(path = "/get/city", method = RequestMethod.GET)
    public List<ResponseEntity<CityResponse>> getAllCities(@RequestParam("name") String name) {
        return bookingService.findAllByCityName(name)
                .stream()
                .map(x -> new CityResponse(x.getCityKey().getCityName(), x.getCityKey().getHotelName()))
                .map(x -> new ResponseEntity<>(x, HttpStatus.FOUND))
                .collect(Collectors.toList());
    }

    @RequestMapping(path = "/add/hotel", method = RequestMethod.POST)
    public ResponseEntity<?> addNewHotel(@RequestBody Hotel hotel) {
        bookingService.saveHotel(hotel);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/add/guest", method = RequestMethod.POST)
    public ResponseEntity<?> addNewGuest(@RequestBody Guest guest) {
        bookingService.saveGuest(guest);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/get/freerooms", method = RequestMethod.GET)
    public ResponseEntity<List<Integer>> getHotelFreeRooms(@RequestParam String hotel,
                                               @RequestParam String city,
                                               @RequestParam LocalDateTime startReverse,
                                               @RequestParam LocalDateTime endReverse) {
       return new ResponseEntity<>(bookingService.getFreeRooms(hotel, city, startReverse, endReverse),
               HttpStatus.FOUND);
    }

    @RequestMapping(path = "/get/bookedrooms", method = RequestMethod.GET)
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
