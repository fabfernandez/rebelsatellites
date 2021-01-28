package com.faba.rebelsatellites.controller;

import com.faba.rebelsatellites.model.Satellite;
import com.faba.rebelsatellites.service.LocationService;
import com.faba.rebelsatellites.service.MessageService;
import com.faba.rebelsatellites.view.LocationAndMessage;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@NoArgsConstructor
public class RebelSatellitesController {
    @Autowired
    private LocationService locationService;
    @Autowired
    private MessageService messageService;

    @GetMapping("/")
    public String ping(){
        return "pong";
    }

    @PostMapping("/topsecret")
    public LocationAndMessage secretTransmission(@RequestBody ArrayList<Satellite> satellites){

        return LocationAndMessage.builder()
                .location(locationService.getLocation(satellites))
                .message(messageService.getMessage(satellites))
                .build();
    }
}
