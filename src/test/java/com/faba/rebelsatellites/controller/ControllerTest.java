package com.faba.rebelsatellites.controller;

import com.faba.rebelsatellites.model.Position;
import com.faba.rebelsatellites.model.Satellite;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            StandardCharsets.UTF_8
    );

    @BeforeAll
    static void beforeAll() throws JsonProcessingException {

    }

    @Test
    void whenThreeSatellitesArriveThenOk() throws Exception {
        //build 3 satellites
        Position enemyPosition = Position.builder().x(100).y(100).build();

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
                .position(Position.builder().x(100).y(-100).build())
                .message(msg1)
                .build();

        Satellite sat2 = Satellite.builder()
                .targetDistance(400)
                .position(Position.builder().x(500).y(100).build())
                .message(msg2)
                .build();

        Satellite sat3 = Satellite.builder()
                .targetDistance(300 * Math.sqrt(5))
                .position(Position.builder().x(-500).y(-200).build())
                .message(msg3)
                .build();

        ArrayList<Satellite> satellites = new ArrayList<>(List.of(sat1, sat2, sat3));

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(satellites);

        //call POST /topsecret
        this.mockMvc.perform(
                post("/topsecret").content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.position.x", is(enemyPosition.getX())))
                .andExpect(jsonPath("$.position.y", is(enemyPosition.getY())))
                .andExpect(jsonPath("$.message.y", is(expectedMessage)))
        ;
    }
}
