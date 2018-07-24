package ru.dataart.courses.cassandra.web.entities;

import ru.dataart.courses.cassandra.repository.entities.hotel.Hotel;

import javax.validation.constraints.NotNull;
import java.util.Set;

public class HotelRequest {
    @NotNull
    private String hotel;
    @NotNull
    private Set<Integer> rooms;
    @NotNull
    private String address;
    @NotNull
    private String city;

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public Set<Integer> getRooms() {
        return rooms;
    }

    public void setRooms(Set<Integer> rooms) {
        this.rooms = rooms;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
