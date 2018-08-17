package ru.dataart.courses.cassandra.repository;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Repository;
import ru.dataart.courses.cassandra.repository.entities.booking.Booking;
import ru.dataart.courses.cassandra.repository.entities.booking.BookingDetail;
import ru.dataart.courses.cassandra.repository.entities.booking.BookingHotelDetail;
import ru.dataart.courses.cassandra.repository.entities.guest.Guest;
import ru.dataart.courses.cassandra.repository.entities.hotel.City;
import ru.dataart.courses.cassandra.repository.entities.hotel.Hotel;
import ru.dataart.courses.cassandra.repository.entities.hotel.Room;

import java.util.Objects;
import java.util.UUID;

@RepositoryProfile
public class SaveRepository {
    public static final Logger log = LoggerFactory.getLogger(SaveRepository.class);

    @Autowired
    private CassandraOperations cassandraOperations;

    //Explanation: Adds a new hotel to the system.
    public void saveHotel(Hotel hotel) {
        cassandraOperations.insert(hotel);
        City city = new City();
        city.getCityKey().setCityName(hotel.getHotelKey().getCityName());
        city.getCityKey().setHotelName(hotel.getHotelKey().getHotelName());
        cassandraOperations.insert(city);
    }

    public void saveCity(City city) {
        cassandraOperations.insert(city);
        Hotel hotel = new Hotel();
        hotel.getHotelKey().setHotelName(city.getCityKey().getHotelName());
        hotel.getHotelKey().setCityName(city.getCityKey().getCityName());
        cassandraOperations.insert(hotel);
    }

    //Explanation: Adds a new guest, who is going to book a room.
    public void saveGuest(Guest guest){
        cassandraOperations.insert(guest);
    }

    //Explanation: Adds a new room to a hotel.
    public void saveRoom(Room room){
        Hotel hotel = cassandraOperations.selectOne(QueryBuilder.select()
                        .from("booking_keyspace", "hotel")
                        .where(QueryBuilder.eq("hotel_name", room.getRoomKey().getHotel())), Hotel.class);
        if(Objects.isNull(hotel)){
            hotel = new Hotel();
            hotel.getHotelKey().setCityName(room.getRoomKey().getCity());
            hotel.getHotelKey().setHotelName(room.getRoomKey().getHotel());
            hotel.getRooms().add(room.getRoomKey().getRoomNumber());
            saveHotel(hotel);
        }
        cassandraOperations.insert(room);
    }

    //Explanation: Adds a new room booking for a guest.
    public void saveBooking(Booking booking){
        cassandraOperations.insert(booking);
    }

    public void saveBookingDetail(BookingDetail bookingDetail, BeginEnd beginEnd){
        cassandraOperations.insert(bookingDetail);
        UUID bookingId = bookingDetail.getBookingId();
        BookingHotelDetail bookingHotelDetail = new BookingHotelDetail();
        bookingHotelDetail.getBookingHotelDetailKey().setHotel(bookingDetail.getHotel());
        bookingHotelDetail.getBookingHotelDetailKey().setCity(bookingDetail.getCity());
        bookingHotelDetail.getBookingHotelDetailKey().setBeginEnd(beginEnd.getName());
        switch (beginEnd){
            case BEGIN:{
                bookingHotelDetail.getBookingHotelDetailKey().setEventDate(bookingDetail.getStart());
                break;
            }
            case END:{
                bookingHotelDetail.getBookingHotelDetailKey().setEventDate(bookingDetail.getEnd());
                break;
            }
        }
        bookingHotelDetail.getBookingHotelDetailKey().setRoomNumber(bookingDetail.getRoomNumber());
        bookingHotelDetail.setBookingId(bookingId);
        cassandraOperations.insert(bookingHotelDetail);
    }

    public Guest getGuestByName(String name){
        return cassandraOperations.selectOne("select * from booking_keyspace.guest where guest_name='" + name + "'", Guest.class);
    }

    public enum BeginEnd{
        BEGIN {
            @Override
            public String getName() {
                return "B";
            }
        },
        END {
            @Override
            public String getName() {
                return "E";
            }
        };

        public abstract String getName();
    }

}
