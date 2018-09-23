package ru.dataart.courses.cassandra.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.dataart.courses.cassandra.repository.entities.booking.BookingDetail;
import ru.dataart.courses.cassandra.repository.entities.booking.BookingHotelDetail;
import ru.dataart.courses.cassandra.repository.entities.booking.BookingHotelDetailKey;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Stream;

@RepositoryProfile
@Repository
public interface BookingHotelDetailRepository extends CassandraRepository<BookingHotelDetail, BookingHotelDetailKey> {

    //Explanation: Gets free rooms in a specific hotel for the current period.
    @Query("select * from booking_hotel_detail where city = :city and hotel = :hotel and event_date >= :start and event_date <= :end")
    List<BookingHotelDetail> findBookedRooms(@Param("city") String city, @Param("hotel") String hotel, @Param("start") Timestamp start, @Param("end") Timestamp end);

    @Query("select * from booking_hotel_detail")
    List<BookingHotelDetail> findAll();
}
