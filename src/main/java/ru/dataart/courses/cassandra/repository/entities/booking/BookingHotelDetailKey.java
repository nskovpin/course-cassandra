package ru.dataart.courses.cassandra.repository.entities.booking;

import com.datastax.driver.core.LocalDate;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@PrimaryKeyClass
public class BookingHotelDetailKey implements Serializable {

    @PrimaryKeyColumn(name = "hotel_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private UUID hotelId;

    @PrimaryKeyColumn(name = "event_date", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private Date eventDate;

    @PrimaryKeyColumn(name = "begin_end", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private String beginEnd;

    @PrimaryKeyColumn(name = "room_number", ordinal = 3, type = PrimaryKeyType.CLUSTERED)
    private Integer roomNumber;

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getBeginEnd() {
        return beginEnd;
    }

    public void setBeginEnd(String beginEnd) {
        this.beginEnd = beginEnd;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookingHotelDetailKey that = (BookingHotelDetailKey) o;

        if (hotelId != null ? !hotelId.equals(that.hotelId) : that.hotelId != null) return false;
        if (eventDate != null ? !eventDate.equals(that.eventDate) : that.eventDate != null) return false;
        if (beginEnd != null ? !beginEnd.equals(that.beginEnd) : that.beginEnd != null) return false;
        return roomNumber != null ? roomNumber.equals(that.roomNumber) : that.roomNumber == null;
    }

    @Override
    public int hashCode() {
        int result = hotelId != null ? hotelId.hashCode() : 0;
        result = 31 * result + (eventDate != null ? eventDate.hashCode() : 0);
        result = 31 * result + (beginEnd != null ? beginEnd.hashCode() : 0);
        result = 31 * result + (roomNumber != null ? roomNumber.hashCode() : 0);
        return result;
    }
}
