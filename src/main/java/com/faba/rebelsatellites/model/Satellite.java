package com.faba.rebelsatellites.model;

import lombok.*;

import java.util.ArrayList;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Satellite {
    Location location;
    double targetDistance;
    ArrayList<String> message;
}
