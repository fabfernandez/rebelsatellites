package com.faba.rebelsatellites.service;

import com.faba.rebelsatellites.exceptions.NotEnoughSatellitesException;
import com.faba.rebelsatellites.model.Satellite;
import com.faba.rebelsatellites.model.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class LocationServiceTest {

    private final LocationService locationService;

    @Autowired
    public LocationServiceTest(LocationService locationService) {
        this.locationService = locationService;
    }

    @Test
    void only2satellitesFails() {
        Satellite sat1 = Satellite.builder().build();
        Satellite sat2 = Satellite.builder().build();

        assertThrows(NotEnoughSatellitesException.class, () -> {
            locationService.getLocation(new ArrayList<>(List.of(sat1, sat2)));
        });
    }

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

    @Test
    void locateEnemy3() {
        Position enemyPosition = Position.builder().x(-1000).y(-600).build();

        Satellite sat1 = Satellite.builder()
                .position(Position.builder().x(500).y(100).build())
                .build();
        sat1.setTargetDistance(calculateDistance(sat1, enemyPosition));

        Satellite sat2 = Satellite.builder()
                .position(Position.builder().x(234).y(40).build())
                .build();
        sat2.setTargetDistance(calculateDistance(sat2, enemyPosition));

        Satellite sat3 = Satellite.builder()
                .position(Position.builder().x(420).y(650).build())
                .build();
        sat3.setTargetDistance(calculateDistance(sat3, enemyPosition));

        ArrayList<Satellite> satellites = new ArrayList<>(List.of(sat1, sat2, sat3));

        Position calculatedPosition = locationService.getLocation(satellites);

        Assertions.assertEquals(enemyPosition.getX(), calculatedPosition.getX());
        Assertions.assertEquals(enemyPosition.getY(), calculatedPosition.getY());
    }

    @Test
    void locateEnemy1Decimal() {
        Position enemyPosition = Position.builder().x(797.5).y(-100.1).build();

        Satellite sat1 = Satellite.builder()
                .position(Position.builder().x(500.2).y(100.6).build())
                .build();
        sat1.setTargetDistance(calculateDistance(sat1, enemyPosition));

        Satellite sat2 = Satellite.builder()
                .position(Position.builder().x(234.7).y(40.9).build())
                .build();
        sat2.setTargetDistance(calculateDistance(sat2, enemyPosition));

        Satellite sat3 = Satellite.builder()
                .position(Position.builder().x(420.1).y(650.2).build())
                .build();
        sat3.setTargetDistance(calculateDistance(sat3, enemyPosition));

        ArrayList<Satellite> satellites = new ArrayList<>(List.of(sat1, sat2, sat3));

        Position calculatedPosition = locationService.getLocation(satellites);

        Assertions.assertEquals(enemyPosition.getX(), calculatedPosition.getX());
        Assertions.assertEquals(enemyPosition.getY(), calculatedPosition.getY());
    }

    @Test
    void locateEnemy4Decimals() {
        Position enemyPosition = Position.builder().x(797.5645).y(-100.1342).build();

        Satellite sat1 = Satellite.builder()
                .position(Position.builder().x(500.2534).y(100.9845).build())
                .build();
        sat1.setTargetDistance(calculateDistance(sat1, enemyPosition));

        Satellite sat2 = Satellite.builder()
                .position(Position.builder().x(234.8167).y(40.8715).build())
                .build();
        sat2.setTargetDistance(calculateDistance(sat2, enemyPosition));

        Satellite sat3 = Satellite.builder()
                .position(Position.builder().x(420.7152).y(650.8163).build())
                .build();
        sat3.setTargetDistance(calculateDistance(sat3, enemyPosition));

        ArrayList<Satellite> satellites = new ArrayList<>(List.of(sat1, sat2, sat3));

        Position calculatedPosition = locationService.getLocation(satellites);

        Assertions.assertEquals(enemyPosition.getX(), calculatedPosition.getX());
        Assertions.assertEquals(enemyPosition.getY(), calculatedPosition.getY());
    }

    //auxiliary methods to generate more tests easily

    private double calculateDistance(Satellite satellite, Position enemyPosition) {
        double distanceOnX = satellite.getPosition().getX() - enemyPosition.getX();
        double distanceOnY = satellite.getPosition().getY() - enemyPosition.getY();
        //Pythagorean theorem
        return Math.sqrt(Math.pow(distanceOnX, 2) + Math.pow(distanceOnY, 2));
    }
}
