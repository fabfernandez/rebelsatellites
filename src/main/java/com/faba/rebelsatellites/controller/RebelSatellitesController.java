package com.faba.rebelsatellites.controller;

import com.faba.rebelsatellites.model.Satellite;
import com.faba.rebelsatellites.service.LocationService;
import com.faba.rebelsatellites.service.MessageService;
import com.faba.rebelsatellites.view.LocationAndMessage;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@NoArgsConstructor
public class RebelSatellitesController {
    private LocationService locationService;
    private MessageService messageService;

    public RebelSatellitesController(LocationService locationService, MessageService messageService) {
        this.locationService = locationService;
        this.messageService = messageService;
    }

    @GetMapping("/")
    public String ping() {
        return "pong";
    }

    @PostMapping(value = "/topsecret", produces = "application/json")
    public LocationAndMessage secretTransmission(@RequestBody ArrayList<Satellite> satellites){

        return LocationAndMessage.builder()
                .location(locationService.getLocation(satellites))
                .message(messageService.getMessage(satellites))
                .build();
    }
}
