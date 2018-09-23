package ru.dataart.courses.cassandra.repository.entities.booking;


import com.datastax.driver.core.LocalDate;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.sql.Date;
import java.util.UUID;

@PrimaryKeyClass
public class BookingDetailKey implements Serializable {

    @PrimaryKeyColumn(name = "guest_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private UUID guestId;

    @PrimaryKeyColumn(name = "day", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private LocalDate day;

    public UUID getGuestId() {
        return guestId;
    }

    public void setGuestId(UUID guestId) {
        this.guestId = guestId;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public BookingDetailKey() {
    }

    public BookingDetailKey(LocalDate day, UUID guestId) {
        this.guestId = guestId;
        this.day = day;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookingDetailKey that = (BookingDetailKey) o;

        if (guestId != null ? !guestId.equals(that.guestId) : that.guestId != null) return false;
        return day != null ? day.equals(that.day) : that.day == null;
    }

    @Override
    public int hashCode() {
        int result = guestId != null ? guestId.hashCode() : 0;
        result = 31 * result + (day != null ? day.hashCode() : 0);
        return result;
    }
}
