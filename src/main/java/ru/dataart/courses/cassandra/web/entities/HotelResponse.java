package ru.dataart.courses.cassandra.web.entities;

public class HotelResponse {

    private String city;

    private String hotel;

    public HotelResponse(String city, String hotel) {
        this.city = city;
        this.hotel = hotel;
    }

    public String getCity() {
        return city;
    }


    public String getHotel() {
        return hotel;
    }
}
