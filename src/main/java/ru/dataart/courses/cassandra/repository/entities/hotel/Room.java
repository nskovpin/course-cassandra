package ru.dataart.courses.cassandra.repository.entities.hotel;

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
