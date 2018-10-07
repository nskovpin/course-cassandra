package ru.dataart.courses.cassandra.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import ru.dataart.courses.cassandra.repository.entities.booking.BookingDetail;
import ru.dataart.courses.cassandra.repository.entities.booking.BookingHotelDetail;
import ru.dataart.courses.cassandra.repository.entities.guest.Guest;
import ru.dataart.courses.cassandra.repository.entities.hotel.Hotel;
import ru.dataart.courses.cassandra.repository.entities.hotel.Room;

import java.util.Objects;

@RepositoryProfile
public class SaveRepository {
    public static final Logger log = LoggerFactory.getLogger(SaveRepository.class);

    @Autowired
    private CassandraOperations cassandraOperations;

    //Explanation: Adds a new hotel to the system.
    public void saveHotel(Hotel hotel) {
        cassandraOperations.insert(hotel);
        hotel.getRooms().stream().map(x-> {
            Room roomDb = new Room();
            roomDb.getRoomKey().setHotelId(hotel.getId());
            roomDb.getRoomKey().setRoomNumber(x);
            return roomDb;
        }).forEach(x -> cassandraOperations.insert(x));
    }

    //Explanation: Adds a new guest, who is going to book a room.
    public void saveGuest(Guest guest){
        cassandraOperations.insert(guest);
    }

    //Explanation: Adds a new room to a hotel.
    public void saveRoom(Room room, Hotel hotel){
        if(!hotel.getRooms().contains(room.getRoomKey().getRoomNumber())){
            updateHotel(hotel, room.getRoomKey().getRoomNumber());
            cassandraOperations.insert(room);
        }
    }

    public void saveBookingDetail(BookingDetail bookingDetail){
        cassandraOperations.insert(bookingDetail);
    }

    public void saveBookingHotelDetail(BookingHotelDetail bookingHotelDetail){
        cassandraOperations.insert(bookingHotelDetail);
    }

    public Guest getGuestByName(String name){
        return cassandraOperations.selectOne("select * from booking_keyspace.guest where guest_name='" + name + "'", Guest.class);
    }

    private void updateHotel(Hotel hotel, Integer room) {
        hotel.getRooms().add(room);
        cassandraOperations.update(hotel);
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
