package ru.dataart.courses.cassandra;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.dataart.courses.cassandra.config.ApplicationConfig;
import ru.dataart.courses.cassandra.repository.entities.guest.Guest;
import ru.dataart.courses.cassandra.repository.entities.hotel.Hotel;
import ru.dataart.courses.cassandra.repository.entities.hotel.Room;
import ru.dataart.courses.cassandra.service.BookingService;
import ru.dataart.courses.cassandra.web.entities.BookingRequest;
import ru.dataart.courses.cassandra.web.entities.GuestRequest;
import ru.dataart.courses.cassandra.web.entities.HotelRequest;
import ru.dataart.courses.cassandra.web.entities.RoomRequest;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@RunWith(SpringRunner.class)
@Import(ApplicationConfig.class)
@AutoConfigureMockMvc
@ActiveProfiles("development")
public class BookingWebTest {

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Before
    public void setup() {
        List<Hotel> hotels = new ArrayList<>();
        Hotel hotel = new Hotel();
        hotel.getHotelKey().setHotel("Hotel N1");
        hotel.getHotelKey().setCity("Moscow");
        hotels.add(hotel);
        Mockito.when(bookingService.findAllByCityName("Moscow")).thenReturn(hotels);
        Mockito.when(bookingService.findAllByCityName("NotExistingCity")).thenReturn(Collections.emptyList());
        Mockito.when(bookingService.saveHotel(any(Hotel.class))).thenReturn(CompletableFuture.completedFuture(Boolean.TRUE));
        Mockito.when(bookingService.saveRoom(any(Room.class), any(Hotel.class))).thenReturn(CompletableFuture.completedFuture(Boolean.TRUE));
        Mockito.when(bookingService.saveGuest(any(Guest.class))).thenReturn(CompletableFuture.completedFuture(Boolean.TRUE));
    }

    @Test
    public void getCityEndpointTest() throws Exception {
        mvc.perform(get(String.format("/api/get/city?name=%s", "Moscow"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(bookingService).findAllByCityName("Moscow");
        Mockito.verifyNoMoreInteractions(bookingService);

        mvc.perform(get(String.format("/api/get/city?name=%s", "NotExistingCity"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(bookingService).findAllByCityName("NotExistingCity");
        Mockito.verifyNoMoreInteractions(bookingService);
    }


    @Test
    public void addHotelEndpointTest() throws Exception {
        HotelRequest hotelRequest = new HotelRequest();
        hotelRequest.setCity("Voronezh");
        hotelRequest.setAddress("Bolshaya");
        hotelRequest.setHotel("BRNO");
        hotelRequest.setRooms(new HashSet<>(Arrays.asList(1, 2, 3)));

        mvc.perform(post("/api/add/hotel")
                .content(mapper.writeValueAsString(hotelRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("address", is("Bolshaya")));
        Mockito.verify(bookingService).saveHotel(any());
        Mockito.verifyNoMoreInteractions(bookingService);
    }


    @Test
    public void addRoomEndpointTest() throws Exception {
        RoomRequest roomRequest = new RoomRequest();
        roomRequest.setCity("Voronezh");
        roomRequest.setRoomNumber(33);
        roomRequest.setHotel("BRNO");

        mvc.perform(post("/api/add/room")
                .content(mapper.writeValueAsString(roomRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("roomKey.roomNumber", is(33)));
    }

    @Test
    public void addGuestEndpointTest() throws Exception {
        GuestRequest guestRequest = new GuestRequest();
        guestRequest.setName("Nikolay");

        mvc.perform(post("/api/add/guest")
                .content(mapper.writeValueAsString(guestRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("guestKey.guestName", is("Nikolay")));
    }


}
