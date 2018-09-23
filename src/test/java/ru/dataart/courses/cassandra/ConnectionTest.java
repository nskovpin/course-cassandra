package ru.dataart.courses.cassandra;

import com.datastax.driver.core.Session;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;
import ru.dataart.courses.cassandra.config.ApplicationConfig;
import ru.dataart.courses.cassandra.repository.entities.booking.BookingDetail;
import ru.dataart.courses.cassandra.repository.BookingDetailRepository;
import ru.dataart.courses.cassandra.repository.SaveRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@SpringBootTest
@RunWith(SpringRunner.class)
@Import(ApplicationConfig.class)
@Profile("integration")
public class ConnectionTest {

    @Autowired
    private Session session;

    @Autowired
    private SaveRepository saveRepository;

    @Autowired
    private BookingDetailRepository bookingDetailRepository;

    @Test
    public void connectionTest() {
//        ResultSet resultSet = session.execute("select * from test");
//        ColumnDefinitions columnDefinitions = resultSet.getColumnDefinitions();
//        Assert.assertNotNull(columnDefinitions);
//
//        Guest guest = saveRepository.getGuestByName("Skovpin NS");
//        Assert.assertNotNull(guest);
//
//        Room room = new Room();
//        room.getRoomKey().setCity("Sochi");
//        room.getRoomKey().setHotel("2 start hotel");
//        room.getRoomKey().setRoomNumber(211);
//        saveRepository.saveRoom(room);

//        Stream<BookingDetail> details = bookingDetailRepository.findAllByGuestAndDate(UUID.fromString("e7c26e68-a8c5-4273-9227-412474ac5496"),
//                Timestamp.valueOf(LocalDateTime.parse("2018-04-19 05:25", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));

//        Stream<BookingDetail> details = bookingDetailRepository.findAllByGuestAndDate(UUID.fromString("e7c26e68-a8c5-4273-9227-412474ac5496"),
//                Date.valueOf(LocalDate.parse("2018-04-22")));
//
//        List<BookingDetail> detailsList =  details.collect(Collectors.toList());
//
//        Assert.assertTrue(detailsList.size() > 0);
    }


}
