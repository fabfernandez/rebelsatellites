package com.faba.rebelsatellites.view        ;

import com.faba.rebelsatellites.model.Location;
import lombok.Builder;

@Builder
public class LocationAndMessage {
    Location location;
    String message;
}
