package ru.dataart.courses.cassandra.entities.hotel;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.HashSet;
import java.util.Set;

@Table(value = "hotel")
public class Hotel {

    @PrimaryKey
    private HotelKey hotelKey;

    @Column
    private Set<Integer> rooms;

    @Column
    private String address;

    public Hotel() {
        this.hotelKey = new HotelKey();
        this.rooms = new HashSet<>();
    }

    public HotelKey getHotelKey() {
        return hotelKey;
    }

    public void setHotelKey(HotelKey hotelKey) {
        this.hotelKey = hotelKey;
    }

    public Set<Integer> getRooms() {
        return rooms;
    }

    public void setRooms(Set<Integer> rooms) {
        this.rooms = rooms;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "hotelKey=" + hotelKey +
                ", rooms=" + rooms +
                ", address='" + address + '\'' +
                '}';
    }
}
