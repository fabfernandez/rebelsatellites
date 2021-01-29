package com.faba.rebelsatellites.view;

import lombok.*;

import java.util.ArrayList;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DistanceAndMessageRequest {
    private double distance;
    private ArrayList<String> message;
}
