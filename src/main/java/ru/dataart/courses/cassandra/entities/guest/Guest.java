package ru.dataart.courses.cassandra.entities.guest;


import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("guest")
public class Guest {

    @PrimaryKey
    private GuestKey guestKey;

    public GuestKey getGuestKey() {
        return guestKey;
    }

    public void setGuestKey(GuestKey guestKey) {
        this.guestKey = guestKey;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "guestKey=" + guestKey +
                '}';
    }
}