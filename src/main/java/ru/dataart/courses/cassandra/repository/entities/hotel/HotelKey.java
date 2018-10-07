package ru.dataart.courses.cassandra.repository.entities.hotel;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;


import java.io.Serializable;
import java.util.Objects;

@PrimaryKeyClass
public class HotelKey implements Serializable {

    @PrimaryKeyColumn(name = "hotel", type = PrimaryKeyType.PARTITIONED, ordinal = 0)
    private String hotel;

    @PrimaryKeyColumn(name = "city", type = PrimaryKeyType.CLUSTERED, ordinal = 1)
    private String city;

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HotelKey hotelKey = (HotelKey) o;
        return Objects.equals(hotel, hotelKey.hotel) &&
                Objects.equals(city, hotelKey.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hotel, city);
    }
}
