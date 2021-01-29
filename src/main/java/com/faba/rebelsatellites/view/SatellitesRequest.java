package com.faba.rebelsatellites.view;

import com.faba.rebelsatellites.model.Satellite;
import lombok.*;

import java.util.ArrayList;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SatellitesRequest {
    private ArrayList<Satellite> satellites;
}
