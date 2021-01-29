package com.faba.rebelsatellites.controller;

import com.faba.rebelsatellites.service.LocationService;
import com.faba.rebelsatellites.service.MessageService;
import com.faba.rebelsatellites.view.LocationAndMessageResponse;
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
        return "pong";
    }

    @PostMapping(value = "/topsecret", produces = "application/json")
    public LocationAndMessageResponse secretTransmission(@RequestBody SatellitesRequest satellitesRequest) {

        return LocationAndMessageResponse.builder()
                .location(locationService.getLocation(satellitesRequest.getSatellites()))
                .message(messageService.getMessage(satellitesRequest.getSatellites()))
                .build();
    }
}
