package com.faba.topSecret.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Satellite {
    Position position;
    double targetDistance;
}
