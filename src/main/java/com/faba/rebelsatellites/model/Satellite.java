package com.faba.rebelsatellites.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Builder
@Getter
@Setter
public class Satellite {
    Location location;
    double targetDistance;
    ArrayList<String> message;
}
