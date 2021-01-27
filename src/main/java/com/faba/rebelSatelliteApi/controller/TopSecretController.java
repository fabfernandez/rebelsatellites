package com.faba.rebelSatelliteApi.controller;

import com.faba.rebelSatelliteApi.model.Satellite;
import com.faba.rebelSatelliteApi.service.LocationService;
import com.faba.rebelSatelliteApi.service.MessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TopSecretController {
    private LocationService locationService;
    private MessageService messageService;

    public TopSecretController(LocationService locationService, MessageService messageService) {
        this.locationService = locationService;
        this.messageService = messageService;
    }

    @GetMapping("/ping")
    public String ping(){
        return "pong";
    }


    @PostMapping("/topsecret")
    public Satellite secretTransmission(){
        return Satellite.builder().build();
    }
}
