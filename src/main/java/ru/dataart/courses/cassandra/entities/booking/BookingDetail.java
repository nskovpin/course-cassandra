package ru.dataart.courses.cassandra.entities.booking;


import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.sql.Timestamp;
import java.util.UUID;

@Table("booking_detail")
public class BookingDetail {

    @PrimaryKey
    private BookingDetailKey bookingDetailKey;

    @Column("booking_id")
    private UUID bookingId;

    @Column("event_start")
    private Timestamp start;

    @Column("event_end")
    private Timestamp end;

    @Column("room_number")
    private Integer roomNumber;

    @Column("hotel")
    private String hotel;

    @Column("city")
    private String city;

    public BookingDetailKey getBookingDetailKey() {
        return bookingDetailKey;
    }

    public void setBookingDetailKey(BookingDetailKey bookingDetailKey) {
        this.bookingDetailKey = bookingDetailKey;
    }

    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

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
}
