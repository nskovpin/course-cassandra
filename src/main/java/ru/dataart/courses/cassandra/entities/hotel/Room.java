package ru.dataart.courses.cassandra.entities.hotel;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

@Table(value = "room")
public class Room {

    @PrimaryKey
    private RoomKey roomKey;

    public Room() {
        this.roomKey = new RoomKey();
    }

    public RoomKey getRoomKey() {
        return roomKey;
    }

    public void setRoomKey(RoomKey roomKey) {
        this.roomKey = roomKey;
    }
}
