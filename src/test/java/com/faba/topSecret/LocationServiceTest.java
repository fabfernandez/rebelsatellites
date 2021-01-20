package com.faba.topSecret;

import com.faba.topSecret.model.Satellite;
import com.faba.topSecret.model.Position;
import com.faba.topSecret.service.LocationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class LocationServiceTest {

    @Autowired
    LocationService locationService;

    @Test
    void locateEnemy() {
        Position enemyPosition = Position.builder().x(100).y(100).build();

        Satellite sat1 = Satellite.builder()
                .targetDistance(200)
                .position(Position.builder().x(100).y(-100).build())
                .build();

        Satellite sat2 = Satellite.builder()
                .targetDistance(400)
                .position(Position.builder().x(500).y(100).build())
                .build();

        Satellite sat3 = Satellite.builder()
                .targetDistance(300 * Math.sqrt(5))
                .position(Position.builder().x(-500).y(-200).build())
                .build();

        ArrayList<Satellite> satellites = new ArrayList<>(List.of(sat1, sat2, sat3));

        Position calculatedPosition = locationService.getLocation(satellites);

        Assertions.assertEquals(enemyPosition.getX(), calculatedPosition.getX());
        Assertions.assertEquals(enemyPosition.getY(), calculatedPosition.getY());
    }

}
