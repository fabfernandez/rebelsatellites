package com.faba.rebelsatellites.repository;

import com.faba.rebelsatellites.exceptions.UnknownSatelliteException;
import com.faba.rebelsatellites.model.Location;
import com.faba.rebelsatellites.repository.SatelliteRepository.KnownSatellites;
import org.springframework.stereotype.Repository;

import java.util.Locale;
import java.util.Map;

@Repository
public class LocationRepository {

    private static final Map<String, Location> locationDictionary = Map.of(
            KnownSatellites.KENOBI.name(), new Location(-500, -200),
            KnownSatellites.SKYWALKER.name(), new Location(100, -100),
            KnownSatellites.SATO.name(), new Location(500, 100)
    );

    public Location getLocationByName(String name) {
        String key = name.toUpperCase(Locale.ROOT);
        if (!locationDictionary.containsKey(key)) {
            throw new UnknownSatelliteException(String.format("Satellite %s not found", key));
        }

        return locationDictionary.get(key);
    }
}
