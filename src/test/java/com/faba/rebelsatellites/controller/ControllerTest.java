package com.faba.rebelsatellites.controller;

import com.faba.rebelsatellites.model.Location;
import com.faba.rebelsatellites.model.Satellite;
import com.faba.rebelsatellites.service.LocationService;
import com.faba.rebelsatellites.service.MessageService;
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
@ContextConfiguration(classes = {RebelSatellitesController.class, LocationService.class, MessageService.class})
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
        Location enemyLocation = Location.builder().x(100).y(100).build();

        String expectedMessage = "we are under attack";

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
                .name("kenobi")
                .message(msg1)
                .build();
        sat1.setTargetDistance(locationService.calculateDistance(sat1, enemyLocation));

        Satellite sat2 = Satellite.builder()
                .name("sato")
                .message(msg2)
                .build();
        sat2.setTargetDistance(locationService.calculateDistance(sat2, enemyLocation));

        Satellite sat3 = Satellite.builder()
                .name("skywalker")
                .message(msg3)
                .build();
        sat3.setTargetDistance(locationService.calculateDistance(sat3, enemyLocation));

        ArrayList<Satellite> satellites = new ArrayList<>(List.of(sat1, sat2, sat3));

        SatellitesRequest satellitesRequest = SatellitesRequest.builder().satellites(satellites).build();

        Gson gson = new Gson();
        var requestBuilder =
                post("/topsecret")
                        .content(gson.toJson(satellitesRequest))
                        .contentType("application/json");

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.position.x", is(enemyLocation.getX())))
                .andExpect(jsonPath("$.position.y", is(enemyLocation.getY())))
                .andExpect(jsonPath("$.message", is(expectedMessage)))
        ;
    }
}
