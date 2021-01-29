package com.faba.rebelsatellites.service;

import com.faba.rebelsatellites.exceptions.NotEnoughSatellitesException;
import com.faba.rebelsatellites.exceptions.UnknownSatelliteException;
import com.faba.rebelsatellites.model.Satellite;
import com.faba.rebelsatellites.model.Location;
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
        ArrayList<Satellite> satellites = new ArrayList<>(List.of(sat1, sat2));

        assertThrows(NotEnoughSatellitesException.class, () -> locationService.getLocation(satellites));
    }

    @Test
    void whenAnUnknownSatelliteIsReceivedThenExceptionIsThrown() {
        Satellite sat1 = Satellite.builder().name("kenobi").build();
        Satellite sat2 = Satellite.builder().name("darth vader").build();
        Satellite sat3 = Satellite.builder().name("sato").build();
        ArrayList<Satellite> satellites = new ArrayList<>(List.of(sat1, sat2, sat3));

        assertThrows(UnknownSatelliteException.class, () -> locationService.getLocation(satellites));
    }

    @Test
    void locateEnemy() {
        Location enemyLocation = Location.builder().x(100).y(100).build();

        Satellite sat1 = Satellite.builder()
                .name("kenobi")
                .build();
        sat1.setTargetDistance(locationService.calculateDistance(sat1, enemyLocation));

        Satellite sat2 = Satellite.builder()
                .name("sato")
                .build();
        sat2.setTargetDistance(locationService.calculateDistance(sat2, enemyLocation));

        Satellite sat3 = Satellite.builder()
                .name("skywalker")
                .build();
        sat3.setTargetDistance(locationService.calculateDistance(sat3, enemyLocation));

        ArrayList<Satellite> satellites = new ArrayList<>(List.of(sat1, sat2, sat3));

        Location calculatedLocation = locationService.getLocation(satellites);

        Assertions.assertEquals(enemyLocation.getX(), calculatedLocation.getX());
        Assertions.assertEquals(enemyLocation.getY(), calculatedLocation.getY());
    }

    @Test
    void locateEnemy2() {
        Location enemyLocation = Location.builder().x(-140).y(40).build();

        Satellite sat1 = Satellite.builder()
                .name("kenobi")
                .build();
        sat1.setTargetDistance(locationService.calculateDistance(sat1, enemyLocation));

        Satellite sat2 = Satellite.builder()
                .name("sato")
                .build();
        sat2.setTargetDistance(locationService.calculateDistance(sat2, enemyLocation));

        Satellite sat3 = Satellite.builder()
                .name("skywalker")
                .build();
        sat3.setTargetDistance(locationService.calculateDistance(sat3, enemyLocation));

        ArrayList<Satellite> satellites = new ArrayList<>(List.of(sat1, sat2, sat3));

        Location calculatedLocation = locationService.getLocation(satellites);

        Assertions.assertEquals(enemyLocation.getX(), calculatedLocation.getX());
        Assertions.assertEquals(enemyLocation.getY(), calculatedLocation.getY());
    }

    @Test
    void locateEnemy3() {
        Location enemyLocation = Location.builder().x(-1000).y(-600).build();

        Satellite sat1 = Satellite.builder()
                .name("kenobi")
                .build();
        sat1.setTargetDistance(locationService.calculateDistance(sat1, enemyLocation));

        Satellite sat2 = Satellite.builder()
                .name("sato")
                .build();
        sat2.setTargetDistance(locationService.calculateDistance(sat2, enemyLocation));

        Satellite sat3 = Satellite.builder()
                .name("skywalker")
                .build();
        sat3.setTargetDistance(locationService.calculateDistance(sat3, enemyLocation));

        ArrayList<Satellite> satellites = new ArrayList<>(List.of(sat1, sat2, sat3));

        Location calculatedLocation = locationService.getLocation(satellites);

        Assertions.assertEquals(enemyLocation.getX(), calculatedLocation.getX());
        Assertions.assertEquals(enemyLocation.getY(), calculatedLocation.getY());
    }

    @Test
    void locateEnemy1Decimal() {
        Location enemyLocation = Location.builder().x(797.5).y(-100.1).build();

        Satellite sat1 = Satellite.builder()
                .name("kenobi")
                .build();
        sat1.setTargetDistance(locationService.calculateDistance(sat1, enemyLocation));

        Satellite sat2 = Satellite.builder()
                .name("sato")
                .build();
        sat2.setTargetDistance(locationService.calculateDistance(sat2, enemyLocation));

        Satellite sat3 = Satellite.builder()
                .name("skywalker")
                .build();
        sat3.setTargetDistance(locationService.calculateDistance(sat3, enemyLocation));

        ArrayList<Satellite> satellites = new ArrayList<>(List.of(sat1, sat2, sat3));

        Location calculatedLocation = locationService.getLocation(satellites);

        Assertions.assertEquals(enemyLocation.getX(), calculatedLocation.getX());
        Assertions.assertEquals(enemyLocation.getY(), calculatedLocation.getY());
    }

    @Test
    void locateEnemy4Decimals() {
        Location enemyLocation = Location.builder().x(797.5645).y(-100.1342).build();

        Satellite sat1 = Satellite.builder()
                .name("kenobi")
                .build();
        sat1.setTargetDistance(locationService.calculateDistance(sat1, enemyLocation));

        Satellite sat2 = Satellite.builder()
                .name("sato")
                .build();
        sat2.setTargetDistance(locationService.calculateDistance(sat2, enemyLocation));

        Satellite sat3 = Satellite.builder()
                .name("skywalker")
                .build();
        sat3.setTargetDistance(locationService.calculateDistance(sat3, enemyLocation));

        ArrayList<Satellite> satellites = new ArrayList<>(List.of(sat1, sat2, sat3));

        Location calculatedLocation = locationService.getLocation(satellites);

        Assertions.assertEquals(enemyLocation.getX(), calculatedLocation.getX());
        Assertions.assertEquals(enemyLocation.getY(), calculatedLocation.getY());
    }
}
