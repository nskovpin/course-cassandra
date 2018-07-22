package ru.dataart.courses.cassandra.repository.entities.hotel;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;

@PrimaryKeyClass
public class RoomKey implements Serializable {

    @PrimaryKeyColumn(name = "room_number", type = PrimaryKeyType.PARTITIONED, ordinal = 0)
    private Integer roomNumber;

    @PrimaryKeyColumn(name = "hotel", type = PrimaryKeyType.PARTITIONED, ordinal = 1)
    private String hotel;

    @PrimaryKeyColumn(name = "city", type = PrimaryKeyType.CLUSTERED, ordinal = 1)
    private String city;

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

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
    public String   toString() {
        return "RoomKey{" +
                "roomNumber=" + roomNumber +
                ", hotel='" + hotel + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
