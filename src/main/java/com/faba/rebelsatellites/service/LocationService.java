package com.faba.rebelsatellites.service;

import com.faba.rebelsatellites.exceptions.NotEnoughDataException;
import com.faba.rebelsatellites.exceptions.NotEnoughSatellitesException;
import com.faba.rebelsatellites.exceptions.UnknownSatelliteException;
import com.faba.rebelsatellites.model.Satellite;
import com.faba.rebelsatellites.model.Location;
import com.faba.rebelsatellites.enumerable.SatelliteNames;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class LocationService {
    private static final String KENOBI = SatelliteNames.kenobi.name();
    private static final String SKYWALKER = SatelliteNames.skywalker.name();
    private static final String SATO = SatelliteNames.sato.name();

    private static final Map<String, Location> locationDictionary = Map.of(
            KENOBI, new Location(-500, -200),
            SKYWALKER, new Location(100, -100),
            SATO, new Location(500, 100)
    );

    private HashMap<String, Double> distanceDictionary =
            Maps.newHashMap(ImmutableMap.of(
                    KENOBI, Double.NaN,
                    SKYWALKER, Double.NaN,
                    SATO, Double.NaN
                    )
            );

    public Location getLocation(ArrayList<Satellite> satellites) {

        if (satellites.size() < 3)
            throw new NotEnoughSatellitesException("Not enough satellites recieved. Minimum is 3.");

        Satellite sat0 = satellites.get(0);
        double d0 = sat0.getTargetDistance();
        Location location0 = getLocationByName(sat0.getName());
        double x0 = location0.getX();
        double y0 = location0.getY();

        Satellite sat1 = satellites.get(1);
        double d1 = sat1.getTargetDistance();
        Location location1 = getLocationByName(sat1.getName());
        double x1 = location1.getX();
        double y1 = location1.getY();

        Satellite sat2 = satellites.get(2);
        double d2 = sat2.getTargetDistance();
        Location location2 = getLocationByName(sat2.getName());
        double x2 = location2.getX();
        double y2 = location2.getY();

        //NOTE: this is the algorithm used here https://www.101computing.net/cell-phone-trilateration-algorithm/

        double a = calculateTwiceDifference(x1, x0);
        double b = calculateTwiceDifference(y1, y0);
        double c = calculatePowersArithmetic(d0, d1, x0, x1, y0, y1, 2);

        double d = calculateTwiceDifference(x2, x1);
        double e = calculateTwiceDifference(y2, y1);
        double f = calculatePowersArithmetic(d1, d2, x1, x2, y1, y2, 2);

        double coordinateX = calculateCoordinate(c, e, f, b, e, a, b, d);
        double coordinateY = calculateCoordinate(c, d, a, f, b, d, a, e);

        return Location.builder().x(coordinateX).y(coordinateY).build();
    }

    public double calculateDistance(Satellite satellite, Location enemyLocation) {
        double distanceOnX = getLocationByName(satellite.getName()).getX() - enemyLocation.getX();
        double distanceOnY = getLocationByName(satellite.getName()).getY() - enemyLocation.getY();
        //Pythagorean theorem
        return Math.sqrt(Math.pow(distanceOnX, 2) + Math.pow(distanceOnY, 2));
    }

    public void putSplitDistance(String name, Double distance) {
        distanceDictionary.put(name, distance);
    }

    public Location getBufferedLocation() {
        if (distanceDictionary.containsValue(Double.NaN)) {
            throw new NotEnoughDataException("Not enough data to get the information");
        }

        Satellite sat1 = Satellite.builder()
                .name(KENOBI)
                .targetDistance(distanceDictionary.get(KENOBI)).build();
        Satellite sat2 = Satellite.builder()
                .name(SKYWALKER)
                .targetDistance(distanceDictionary.get(SKYWALKER)).build();
        Satellite sat3 = Satellite.builder()
                .name(SATO)
                .targetDistance(distanceDictionary.get(SATO)).build();
        ArrayList<Satellite> satellites = new ArrayList<>(List.of(sat1, sat2, sat3));

        return getLocation(satellites);
    }

    private Location getLocationByName(String name) {
        String key = name.toLowerCase(Locale.ROOT);
        if (!locationDictionary.containsKey(key)) {
            throw new UnknownSatelliteException(String.format("Satellite %s not found", key));
        }

        return locationDictionary.get(key);
    }

    private double calculateTwiceDifference(double a, double b) {
        return 2 * a - 2 * b;
    }

    private double calculatePowersArithmetic(double a, double b, double c, double d, double e, double f, int power) {
        return Math.pow(a, power) -
                Math.pow(b, power) -
                Math.pow(c, power) +
                Math.pow(d, power) -
                Math.pow(e, power) +
                Math.pow(f, power);
    }

    private double calculateCoordinate
            (double var1, double var2, double var3, double var4, double var5, double var6, double var7, double var8) {
        return round(
                (var1 * var2 - var3 * var4) / (var5 * var6 - var7 * var8),
                10);
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bigDecimal = BigDecimal.valueOf(value);
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }


}
