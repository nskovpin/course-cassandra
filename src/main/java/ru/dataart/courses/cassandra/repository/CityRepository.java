package ru.dataart.courses.cassandra.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import ru.dataart.courses.cassandra.repository.entities.hotel.City;
import ru.dataart.courses.cassandra.repository.entities.hotel.CityKey;

import java.util.List;

@RepositoryProfile
public interface CityRepository extends CassandraRepository<City, CityKey> {

    //Explanation: Get all hotels of a current city
    @Query("select * from city where city_name=?0")
    List<City> findAllByCityName(String cityName);

}
