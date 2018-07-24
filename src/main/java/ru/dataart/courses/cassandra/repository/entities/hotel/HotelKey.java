package ru.dataart.courses.cassandra.repository.entities.hotel;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;


import java.io.Serializable;
import java.util.Objects;

@PrimaryKeyClass
public class HotelKey implements Serializable {

    @PrimaryKeyColumn(name = "hotel_name", type = PrimaryKeyType.PARTITIONED, ordinal = 0)
    private String hotelName;

    @PrimaryKeyColumn(name = "city_name", type = PrimaryKeyType.CLUSTERED, ordinal = 1)
    private String cityName;

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HotelKey hotelKey = (HotelKey) o;
        return Objects.equals(hotelName, hotelKey.hotelName) &&
                Objects.equals(cityName, hotelKey.cityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hotelName, cityName);
    }
}
