package com.faba.rebelsatellites.model;

import lombok.*;

import java.util.ArrayList;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Satellite {
    String name;
    double targetDistance;
    ArrayList<String> message;
}
