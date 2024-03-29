package com.faba.rebelsatellites.model;

import lombok.*;

import java.util.ArrayList;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Satellite {
    private String name;
    private double targetDistance;
    private ArrayList<String> message;
}
