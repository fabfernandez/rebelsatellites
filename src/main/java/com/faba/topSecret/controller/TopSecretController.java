package com.faba.topSecret.controller;

import com.faba.topSecret.model.Satellite;
import com.faba.topSecret.service.LocationService;
import com.faba.topSecret.service.MessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

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
