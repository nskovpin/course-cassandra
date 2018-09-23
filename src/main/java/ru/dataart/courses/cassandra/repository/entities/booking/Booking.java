package ru.dataart.courses.cassandra.repository.entities.booking;


import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Objects;
import java.util.UUID;

@Table("booking")
public class Booking {

    @PrimaryKey
    private BookingKey bookingKey;

    @Column("guest_id")
    private UUID guestId;

    @Column
    private String comment;

    public Booking() {
        this.bookingKey = new BookingKey();
    }

    public BookingKey getBookingKey() {
        return bookingKey;
    }

    public void setBookingKey(BookingKey bookingKey) {
        this.bookingKey = bookingKey;
    }

    public UUID getGuestId() {
        return guestId;
    }

    public void setGuestId(UUID guestId) {
        this.guestId = guestId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(bookingKey, booking.bookingKey) &&
                Objects.equals(guestId, booking.guestId) &&
                Objects.equals(comment, booking.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingKey, guestId, comment);
    }
}
