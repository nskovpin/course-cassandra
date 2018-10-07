package ru.dataart.courses.cassandra.repository.entities.hotel;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.util.UUID;

@PrimaryKeyClass
public class RoomKey implements Serializable {

    @PrimaryKeyColumn(name = "room_number", type = PrimaryKeyType.PARTITIONED, ordinal = 0)
    private Integer roomNumber;

    @PrimaryKeyColumn(name = "hotel_id", type = PrimaryKeyType.CLUSTERED, ordinal = 1)
    private UUID hotelId;

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public UUID getHotelId() {
        return hotelId;
    }

    public void setHotelId(UUID hotelId) {
        this.hotelId = hotelId;
    }
}
