package com.faba.rebelsatellites.view        ;

import com.faba.rebelsatellites.model.Position;
import lombok.Builder;

import java.util.ArrayList;

@Builder
public class PositionAndMessage {
    Position position;
    String message;
}
