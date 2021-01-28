package com.faba.rebelsatellites.model;

import lombok.Builder;

import java.util.ArrayList;

@Builder
public class PositionAndMessage {
    Position position;
    ArrayList<String> capturedMessage;
}
