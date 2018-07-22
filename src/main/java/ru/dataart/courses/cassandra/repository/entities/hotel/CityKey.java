package ru.dataart.courses.cassandra.repository.entities.hotel;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.util.Objects;


@PrimaryKeyClass
public class CityKey implements Serializable {

    @PrimaryKeyColumn(name = "city_name", type = PrimaryKeyType.PARTITIONED, ordinal = 0)
    private String cityName;

    @PrimaryKeyColumn(name = "hotel_name", type = PrimaryKeyType.CLUSTERED, ordinal = 1, ordering = Ordering.ASCENDING)
    private String hotelName;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityKey cityKey = (CityKey) o;
        return Objects.equals(cityName, cityKey.cityName) &&
                Objects.equals(hotelName, cityKey.hotelName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityName, hotelName);
    }

    @Override
    public String toString() {
        return "CityKey{" +
                "cityName='" + cityName + '\'' +
                ", hotelName='" + hotelName + '\'' +
                '}';
    }
}
