package com.faba.rebelsatellites.controller;

import com.faba.rebelsatellites.model.PositionAndMessage;
import com.faba.rebelsatellites.model.Satellite;
import com.faba.rebelsatellites.service.LocationService;
import com.faba.rebelsatellites.service.MessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class RebelSatellitesController {
    private LocationService locationService;
    private MessageService messageService;

    public RebelSatellitesController(LocationService locationService, MessageService messageService) {
        this.locationService = locationService;
        this.messageService = messageService;
    }

    @GetMapping("/ping")
    public String ping(){
        return "pong";
    }

    @PostMapping("/topsecret")
    public PositionAndMessage secretTransmission(@RequestBody ArrayList<Satellite> satellites){
        //get the satellites location
        locationService.getLocation(satellites);
        //get the message
        messageService.getMessage(satellites);
        return PositionAndMessage.builder().build();
    }
}
