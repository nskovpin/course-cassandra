package ru.dataart.courses.cassandra;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.dataart.courses.cassandra.config.ApplicationConfig;
import ru.dataart.courses.cassandra.repository.entities.hotel.City;
import ru.dataart.courses.cassandra.service.BookingService;
import ru.dataart.courses.cassandra.web.CassandraRestApi;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@RunWith(SpringRunner.class)
@Import(ApplicationConfig.class)
public class BookingWebTest {

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CassandraRestApi cassandraRestApi;

    private String cityName = "Moscow";

    @Before
    public void setup(){
        List<City> cities = new ArrayList<>();
        City city = new City();
        city.getCityKey().setCityName(cityName);
        city.getCityKey().setHotelName("Hotel N1");
        cities.add(city);
        Mockito.when(bookingService.findAllByCityName(cityName)).thenReturn(cities);
    }

    @Test
    public void endpointsTest() throws Exception {
        mvc.perform(get(String.format("/get/city?name=%s", cityName))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

    }

}
