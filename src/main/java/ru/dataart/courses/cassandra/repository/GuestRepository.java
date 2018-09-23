package ru.dataart.courses.cassandra.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import ru.dataart.courses.cassandra.repository.entities.guest.Guest;
import ru.dataart.courses.cassandra.repository.entities.guest.GuestKey;

@RepositoryProfile
@Repository
public interface GuestRepository extends CassandraRepository<Guest, GuestKey> {

    @Query("select * from guest where guest_name=?0")
    Guest findOneByGuestName(String guestName);


}
