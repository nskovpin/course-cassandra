package ru.dataart.courses.cassandra.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.dataart.courses.cassandra.entities.booking.BookingDetail;
import ru.dataart.courses.cassandra.entities.booking.BookingHotelDetail;
import ru.dataart.courses.cassandra.entities.booking.BookingHotelDetailKey;

import java.sql.Timestamp;
import java.util.UUID;
import java.util.stream.Stream;

public interface BookingHotelDetailRepository extends CassandraRepository<BookingHotelDetail, BookingHotelDetailKey> {

    //Explanation: Gets free rooms in a specific hotel for the current period.
    @Query("select * from booking_detail where city = :city and hotel = :hotel and event_date >= :start and event_date <= :end")
    Stream<BookingDetail> findBookedRooms(@Param("city") String city, @Param("hotel") String hotel, @Param("start") Timestamp start, @Param("end") Timestamp end);
}
