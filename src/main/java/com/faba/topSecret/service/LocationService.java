package com.faba.topSecret.service;

import com.faba.topSecret.model.Satellite;
import com.faba.topSecret.model.Position;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;

@Service
public class LocationService {

    public Position getLocation(ArrayList<Satellite> satellites) {

        //just in case we get more than 3 satellites, lets sort by target distance.
        satellites.sort(Comparator.comparingDouble(Satellite::getTargetDistance));

        //TODO: si la lista no tiene 3 o mas elementos lanzar excepcion
        //TODO: hacer un exceptionHandler

        double d1 = satellites.get(0).getTargetDistance();
        double x1 = satellites.get(0).getPosition().getX();
        double y1 = satellites.get(0).getPosition().getY();

        double d2 = satellites.get(1).getTargetDistance();
        double x2 = satellites.get(1).getPosition().getX();
        double y2 = satellites.get(1).getPosition().getY();

        double d3 = satellites.get(2).getTargetDistance();
        double x3 = satellites.get(2).getPosition().getX();
        double y3 = satellites.get(2).getPosition().getY();

        //this is the algorithm i'm using: https://www.101computing.net/cell-phone-trilateration-algorithm/

        double a = calculateTwiceDifference(x2, x1);
        double b = calculateTwiceDifference(y2, y1);
        double c = calculatePowersDifference(d1, d2, x1, x2, y1, y2, 2);

        double d = calculateTwiceDifference(x3, x2);
        double e = calculateTwiceDifference(y3, y2);
        double f = calculatePowersDifference(d2, d3, x2, x3, y2, y3, 2);

        double coordinateX = calculateCoordinate(c, e, f, b, e, a, b, d);
        double coordinateY = calculateCoordinate(c, d, a, f, b, d, a, e);

        return Position.builder().x(coordinateX).y(coordinateY).build();
    }

    private double calculateTwiceDifference(double a, double b) {
        return 2 * a - 2 * b;
    }

    private double calculatePowersDifference(double a, double b, double c, double d, double e, double f, int power) {
        return Math.pow(a, power) -
                Math.pow(b, power) -
                Math.pow(c, power) +
                Math.pow(d, power) -
                Math.pow(e, power) +
                Math.pow(f, power);
    }

    private double calculateCoordinate
            (double var1, double var2, double var3, double var4, double var5, double var6, double var7, double var8) {
        return round((var1 * var2 - var3 * var4) / (var5 * var6 - var7 * var8), 10);
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
