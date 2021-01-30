package com.faba.rebelsatellites.controller;

import com.faba.rebelsatellites.model.Satellite;
import com.faba.rebelsatellites.repository.SatelliteRepository;
import com.faba.rebelsatellites.service.LocationService;
import com.faba.rebelsatellites.service.MessageService;
import com.faba.rebelsatellites.view.DistanceAndMessageRequest;
import com.faba.rebelsatellites.view.PositionAndMessageResponse;
import com.faba.rebelsatellites.view.SatellitesRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class RebelSatellitesController {

    private final LocationService locationService;
    private final MessageService messageService;
    private final SatelliteRepository satelliteRepository;

    @Autowired
    public RebelSatellitesController(LocationService locationService,
                                     MessageService messageService,
                                     SatelliteRepository satelliteRepository) {
        this.locationService = locationService;
        this.messageService = messageService;
        this.satelliteRepository = satelliteRepository;
    }

    @GetMapping("/")
    public String ping() {
        return "Pong! this API is up and running.";
    }

    @PostMapping(value = "/topsecret/", produces = "application/json")
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

        Satellite satellite = Satellite.builder()
                .name(satelliteName)
                .message(distanceAndMessageRequest.getMessage())
                .targetDistance(distanceAndMessageRequest.getDistance())
                .build();

        satelliteRepository.add(satellite);

        return "OK ".concat(satelliteName).concat(" received.");
    }

    @GetMapping(value = "/topsecret_split/", produces = "application/json")
    public PositionAndMessageResponse topSecretSplitGet() {
        PositionAndMessageResponse response = PositionAndMessageResponse.builder()
                .position(locationService.getLocation(satelliteRepository.getSatellites()))
                .message(messageService.getMessage(satelliteRepository.getSatellites()))
                .build();
        satelliteRepository.clear();
        return response;
    }
}
