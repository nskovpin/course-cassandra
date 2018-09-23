package ru.dataart.courses.cassandra.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.dataart.courses.cassandra.repository.entities.hotel.Hotel;
import ru.dataart.courses.cassandra.repository.entities.hotel.HotelKey;

@RepositoryProfile
@Repository
public interface HotelRepository  extends CassandraRepository<Hotel, HotelKey> {

    @Query("select * from hotel where hotel_name = :hotel and city_name = :city")
    Hotel findHotelByCityAndName(@Param("hotel") String hotel, @Param("city") String city);
}
