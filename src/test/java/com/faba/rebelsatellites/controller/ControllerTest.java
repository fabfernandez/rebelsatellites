package com.faba.rebelsatellites.controller;

import com.faba.rebelsatellites.model.Location;
import com.faba.rebelsatellites.model.Satellite;
import com.faba.rebelsatellites.service.LocationService;
import com.faba.rebelsatellites.service.MessageService;
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

    @Test
    void ping() throws Exception {

        this.mockMvc
                .perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    void whenThreeSatellitesArriveThenOk() throws Exception {
        //build 3 satellites
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
                .targetDistance(200)
                .location(Location.builder().x(100).y(-100).build())
                .message(msg1)
                .build();
        Satellite sat2 = Satellite.builder()
                .targetDistance(400)
                .location(Location.builder().x(500).y(100).build())
                .message(msg2)
                .build();
        Satellite sat3 = Satellite.builder()
                .targetDistance(300 * Math.sqrt(5))
                .location(Location.builder().x(-500).y(-200).build())
                .message(msg3)
                .build();

        ArrayList<Satellite> satellites = new ArrayList<>(List.of(sat1, sat2, sat3));

        Gson gson = new Gson();

        var requestBuilder =
                post("/topsecret")
                        .content(gson.toJson(satellites))
                        .contentType("application/json");

        //call POST /topsecret and expect location and message
        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location.x", is(enemyLocation.getX())))
                .andExpect(jsonPath("$.location.y", is(enemyLocation.getY())))
                .andExpect(jsonPath("$.message", is(expectedMessage)))
        ;
    }
}
