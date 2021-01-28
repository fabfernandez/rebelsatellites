package com.faba.rebelsatellites.view        ;

import com.faba.rebelsatellites.model.Location;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LocationAndMessage {
    Location location;
    String message;
}
