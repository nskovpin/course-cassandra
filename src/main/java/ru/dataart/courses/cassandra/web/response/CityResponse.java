package ru.dataart.courses.cassandra.web.response;

public class CityResponse {

    private String city;

    private String hotel;

    public CityResponse(String city, String hotel) {
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
