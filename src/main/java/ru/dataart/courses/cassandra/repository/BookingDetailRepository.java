package ru.dataart.courses.cassandra.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.dataart.courses.cassandra.entities.booking.BookingDetail;
import ru.dataart.courses.cassandra.entities.booking.BookingDetailKey;

import java.sql.Date;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public interface BookingDetailRepository extends CassandraRepository<BookingDetail, BookingDetailKey> {

    //Explanation: Guest has booked some room / rooms. Gets booked room / rooms by the specific date and guest number.
    @Query("select * from booking_detail where guest_id = :guest and day = :date")
    Stream<BookingDetail> findAllByGuestAndDate(@Param("guest") UUID guestId, @Param("date") Date day);

}
