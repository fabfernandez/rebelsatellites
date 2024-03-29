package com.faba.rebelsatellites.controller;

import com.faba.rebelsatellites.model.Location;
import com.faba.rebelsatellites.model.Satellite;
import com.faba.rebelsatellites.repository.LocationRepository;
import com.faba.rebelsatellites.repository.SatelliteRepository;
import com.faba.rebelsatellites.service.LocationService;
import com.faba.rebelsatellites.service.MessageService;
import com.faba.rebelsatellites.view.DistanceAndMessageRequest;
import com.faba.rebelsatellites.view.SatellitesRequest;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {
        RebelSatellitesController.class,
        LocationService.class,
        MessageService.class,
        SatelliteRepository.class,
        LocationRepository.class
})
@WebMvcTest
class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LocationService locationService;

    @Test
    void ping() throws Exception {

        this.mockMvc
                .perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    void whenThreeSatellitesArriveThenOk() throws Exception {
        //Given
        Location enemyLocation = Location.builder().x(100).y(100).build();
        String expectedMessage = "we are under attack";
        //When
        ArrayList<String> msg1 = new ArrayList<>(
                List.of("", "", "", "", "", "",  //lag
                        "we", "", "", "attack")
        );
        ArrayList<String> msg2 = new ArrayList<>(
                List.of("", "", "", //lag
                        "", "", "", "")
        );
        ArrayList<String> msg3 = new ArrayList<>(
                List.of("", "", "", "", "", "", "", "", "", "", "", //lag
                        "", "are", "under", "")
        );
        Satellite sat1 = Satellite.builder()
                .name(SatelliteRepository.KnownSatellites.KENOBI.name())
                .message(msg1)
                .build();
        sat1.setTargetDistance(locationService.calculateDistance(sat1, enemyLocation));

        Satellite sat2 = Satellite.builder()
                .name(SatelliteRepository.KnownSatellites.SATO.name())
                .message(msg2)
                .build();
        sat2.setTargetDistance(locationService.calculateDistance(sat2, enemyLocation));

        Satellite sat3 = Satellite.builder()
                .name(SatelliteRepository.KnownSatellites.SKYWALKER.name())
                .message(msg3)
                .build();
        sat3.setTargetDistance(locationService.calculateDistance(sat3, enemyLocation));

        ArrayList<Satellite> satellites = new ArrayList<>(List.of(sat1, sat2, sat3));

        SatellitesRequest satellitesRequest = SatellitesRequest.builder().satellites(satellites).build();

        Gson gson = new Gson();
        var requestBuilder =
                post("/topsecret/")
                        .content(gson.toJson(satellitesRequest))
                        .contentType("application/json");
        //Then
        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.position.x", is(enemyLocation.getX())))
                .andExpect(jsonPath("$.position.y", is(enemyLocation.getY())))
                .andExpect(jsonPath("$.message", is(expectedMessage)))
        ;
    }

    @Test
    void oneSatellitePosts() throws Exception {
        //Given
        Location enemyLocation = Location.builder().x(100).y(100).build();
        //When
        ArrayList<String> msg1 = new ArrayList<>(
                List.of("", "", "", "", "", "",  //lag
                        "we", "", "", "attack")
        );
        Satellite sat1 = Satellite.builder()
                .name(SatelliteRepository.KnownSatellites.KENOBI.name())
                .build();

        DistanceAndMessageRequest distanceAndMessageRequest = DistanceAndMessageRequest.builder()
                .distance(locationService.calculateDistance(sat1, enemyLocation))
                .message(msg1)
                .build();

        Gson gson = new Gson();
        var requestBuilder =
                post("/topsecret_split/" + sat1.getName())
                        .content(gson.toJson(distanceAndMessageRequest))
                        .contentType("application/json");
        //Then
        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    void threeSatellitesPostThenGet() throws Exception {
        //Given
        Location enemyLocation = Location.builder().x(100).y(100).build();
        String expectedMessage = "we are under attack";

        //When
        ArrayList<String> msg1 = new ArrayList<>(
                List.of("", "", "", "", "", "", "we", "", "", "attack"));
        ArrayList<String> msg2 = new ArrayList<>(
                List.of("", "", "", "", "", "", ""));
        ArrayList<String> msg3 = new ArrayList<>(
                List.of("", "", "", "", "", "", "", "", "", "", "", "", "are", "under", ""));

        Satellite sat1 = Satellite.builder().name(SatelliteRepository.KnownSatellites.KENOBI.name()).build();
        Satellite sat2 = Satellite.builder().name(SatelliteRepository.KnownSatellites.SATO.name()).build();
        Satellite sat3 = Satellite.builder().name(SatelliteRepository.KnownSatellites.SKYWALKER.name()).build();

        DistanceAndMessageRequest distanceAndMessageRequest1 = DistanceAndMessageRequest.builder()
                .distance(locationService.calculateDistance(sat1, enemyLocation))
                .message(msg1)
                .build();

        DistanceAndMessageRequest distanceAndMessageRequest2 = DistanceAndMessageRequest.builder()
                .distance(locationService.calculateDistance(sat2, enemyLocation))
                .message(msg2)
                .build();

        DistanceAndMessageRequest distanceAndMessageRequest3 = DistanceAndMessageRequest.builder()
                .distance(locationService.calculateDistance(sat3, enemyLocation))
                .message(msg3)
                .build();

        Gson gson = new Gson();

        //Then
        var requestBuilderPost1 =
                post("/topsecret_split/" + sat1.getName())
                        .content(gson.toJson(distanceAndMessageRequest1))
                        .contentType("application/json");
        this.mockMvc.perform(requestBuilderPost1)
                .andExpect(status().isOk());

        var requestBuilderPost2 =
                post("/topsecret_split/" + sat2.getName())
                        .content(gson.toJson(distanceAndMessageRequest2))
                        .contentType("application/json");
        this.mockMvc.perform(requestBuilderPost2)
                .andExpect(status().isOk());

        var requestBuilderPost3 =
                post("/topsecret_split/" + sat3.getName())
                        .content(gson.toJson(distanceAndMessageRequest3))
                        .contentType("application/json");
        this.mockMvc.perform(requestBuilderPost3)
                .andExpect(status().isOk());


        var requestBuilderGet =
                get("/topsecret_split/");

        this.mockMvc.perform(requestBuilderGet)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.position.x", is(enemyLocation.getX())))
                .andExpect(jsonPath("$.position.y", is(enemyLocation.getY())))
                .andExpect(jsonPath("$.message", is(expectedMessage)))
        ;

    }

}
