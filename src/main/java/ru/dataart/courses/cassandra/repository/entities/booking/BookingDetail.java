package ru.dataart.courses.cassandra.repository.entities.booking;


import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Table("booking_detail")
public class BookingDetail {

    @PrimaryKey
    private BookingDetailKey bookingDetailKey;

    @Column("start")
    private Date start;

    @Column("end")
    private Date end;

    @Column("room_number")
    private Integer roomNumber;

    @Column("hotel_id")
    private UUID hotelId;

    public BookingDetailKey getBookingDetailKey() {
        return bookingDetailKey;
    }

    public void setBookingDetailKey(BookingDetailKey bookingDetailKey) {
        this.bookingDetailKey = bookingDetailKey;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

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
