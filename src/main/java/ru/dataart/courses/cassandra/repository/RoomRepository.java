package ru.dataart.courses.cassandra.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import ru.dataart.courses.cassandra.repository.entities.hotel.Room;
import ru.dataart.courses.cassandra.repository.entities.hotel.RoomKey;

import java.util.UUID;

@Repository
@RepositoryProfile
public interface RoomRepository  extends CassandraRepository<Room, RoomKey> {

    @Query("select * from room where room_number=?0 and hotel_id=?1")
    Room findOneByRoomAndHotel(Integer roomNumber, UUID hotelId);
}
