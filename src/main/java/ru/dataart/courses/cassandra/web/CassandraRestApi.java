package ru.dataart.courses.cassandra.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dataart.courses.cassandra.entities.guest.Guest;
import ru.dataart.courses.cassandra.entities.hotel.City;
import ru.dataart.courses.cassandra.entities.hotel.Hotel;
import ru.dataart.courses.cassandra.service.BookingService;

import java.util.List;
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
    public List<ResponseEntity<City>> getAllCities(@RequestParam("name") String name){
       return bookingService.findAllByCityName(name)
                .stream()
                .map(x -> new ResponseEntity<>(x, HttpStatus.FOUND))
                .collect(Collectors.toList());
    }

    @RequestMapping(path = "/add/hotel", method = RequestMethod.POST)
    public ResponseEntity addNewHotel(@RequestBody Hotel hotel){
        bookingService.saveHotel(hotel);
        return new ResponseEntity(HttpStatus.CREATED);
    }
    @RequestMapping(path = "/add/guest", method = RequestMethod.POST)
    public ResponseEntity addNewGuest(@RequestBody Guest guest){
        bookingService.saveGuest(guest);
        return new ResponseEntity(HttpStatus.CREATED);
    }

}
