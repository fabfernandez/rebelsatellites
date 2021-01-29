package com.faba.rebelsatellites.controller;

import com.faba.rebelsatellites.service.LocationService;
import com.faba.rebelsatellites.service.MessageService;
import com.faba.rebelsatellites.view.PositionAndMessageResponse;
import com.faba.rebelsatellites.view.SatellitesRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class RebelSatellitesController {

    private LocationService locationService;
    private MessageService messageService;

    @Autowired
    public RebelSatellitesController(LocationService locationService, MessageService messageService) {
        this.locationService = locationService;
        this.messageService = messageService;
    }

    @GetMapping("/")
    public String ping() {
        return "Pong! API is up and running.";
    }

    @PostMapping(value = "/topsecret", produces = "application/json")
    public PositionAndMessageResponse secretTransmission(@RequestBody SatellitesRequest satellitesRequest) {

        return PositionAndMessageResponse.builder()
                .position(locationService.getLocation(satellitesRequest.getSatellites()))
                .message(messageService.getMessage(satellitesRequest.getSatellites()))
                .build();
    }
}
