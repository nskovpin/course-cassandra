package ru.dataart.courses.cassandra.repository.entities.booking;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table("booking_hotel_detail")
public class BookingHotelDetail {

    @PrimaryKey
    private BookingHotelDetailKey bookingHotelDetailKey;

    public BookingHotelDetail() {
        this.bookingHotelDetailKey = new BookingHotelDetailKey();
    }

    public BookingHotelDetailKey getBookingHotelDetailKey() {
        return bookingHotelDetailKey;
    }

    public void setBookingHotelDetailKey(BookingHotelDetailKey bookingHotelDetailKey) {
        this.bookingHotelDetailKey = bookingHotelDetailKey;
    }
}
