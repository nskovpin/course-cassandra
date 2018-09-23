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

@PrimaryKeyClass
public class BookingHotelDetailKey implements Serializable {

    @PrimaryKeyColumn(name = "hotel", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String hotel;

    @PrimaryKeyColumn(name = "city", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private String city;

    @PrimaryKeyColumn(name = "event_date", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private Date eventDate;

    @PrimaryKeyColumn(name = "begin_end", ordinal = 3, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private String beginEnd;

    @PrimaryKeyColumn(name = "room_number", ordinal = 4, type = PrimaryKeyType.CLUSTERED)
    private Integer roomNumber;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingHotelDetailKey that = (BookingHotelDetailKey) o;
        return Objects.equals(hotel, that.hotel) &&
                Objects.equals(city, that.city) &&
                Objects.equals(eventDate, that.eventDate) &&
                Objects.equals(beginEnd, that.beginEnd) &&
                Objects.equals(roomNumber, that.roomNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hotel, city, eventDate, beginEnd, roomNumber);
    }
}
