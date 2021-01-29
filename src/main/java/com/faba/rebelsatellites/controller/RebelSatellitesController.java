package com.faba.rebelsatellites.controller;

import com.faba.rebelsatellites.service.LocationService;
import com.faba.rebelsatellites.service.MessageService;
import com.faba.rebelsatellites.view.DistanceAndMessageRequest;
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
    public PositionAndMessageResponse topSecretPost(@RequestBody SatellitesRequest satellitesRequest) {

        return PositionAndMessageResponse.builder()
                .position(locationService.getLocation(satellitesRequest.getSatellites()))
                .message(messageService.getMessage(satellitesRequest.getSatellites()))
                .build();
    }

    @PostMapping(value = "/topsecret_split/{satelliteName}")
    public String topSecretSplitPost(
            @RequestBody DistanceAndMessageRequest distanceAndMessageRequest,
            @PathVariable String satelliteName) {

        //send distance and name to locationService
        //send message and name? to messageService

        return "OK ".concat(satelliteName).concat(" recieved.");
    }

    @GetMapping(value = "/topsecret_split", produces = "application/json")
    public PositionAndMessageResponse topSecretSplitGet() {
        return PositionAndMessageResponse.builder()
                //.position(locationService.getBufferedLocation())
                //.message(messageService.getBufferedMessage())
                .build();
    }
}
