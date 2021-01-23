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

    @Test
    void locateEnemy2() {
        Position enemyPosition = Position.builder().x(-140).y(40).build();

        Satellite sat1 = Satellite.builder()
                .position(Position.builder().x(100).y(100).build())
                .build();
        sat1.setTargetDistance(calculateDistance(sat1, enemyPosition));

        Satellite sat2 = Satellite.builder()
                .position(Position.builder().x(120).y(40).build())
                .build();
        sat2.setTargetDistance(calculateDistance(sat2, enemyPosition));

        Satellite sat3 = Satellite.builder()
                .position(Position.builder().x(-60).y(-100).build())
                .build();
        sat3.setTargetDistance(calculateDistance(sat3, enemyPosition));

        ArrayList<Satellite> satellites = new ArrayList<>(List.of(sat1, sat2, sat3));

        Position calculatedPosition = locationService.getLocation(satellites);

        Assertions.assertEquals(enemyPosition.getX(), calculatedPosition.getX());
        Assertions.assertEquals(enemyPosition.getY(), calculatedPosition.getY());
    }

    //TODO: hacer test con 2 satelites que espere una excepcion

    //auxiliary methods to generate more tests easily

    private double calculateDistance(Satellite satellite, Position enemyPosition) {
        double distanceOnX = satellite.getPosition().getX() - enemyPosition.getX();
        double distanceOnY = satellite.getPosition().getY()- enemyPosition.getY();

        //Pythagorean theorem
        return Math.sqrt(Math.pow(distanceOnX, 2) + Math.pow(distanceOnY, 2));
    }
}
