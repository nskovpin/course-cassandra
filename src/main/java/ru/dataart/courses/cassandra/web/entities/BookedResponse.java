package ru.dataart.courses.cassandra.web.entities;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class BookedResponse {

    private UUID hotelId;
    private Integer roomNumber;
    private LocalDate start;
    private LocalDate end;

    public UUID getHotelId() {
        return hotelId;
    }

    public void setHotelId(UUID hotelId) {
        this.hotelId = hotelId;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }
}
