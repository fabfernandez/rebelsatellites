package com.faba.rebelsatellites.repository;

import com.faba.rebelsatellites.exceptions.UnknownSatelliteException;
import com.faba.rebelsatellites.model.Satellite;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Locale;

@Repository
public class SatelliteRepository {
    //if there is a new satellite, add it here and put its location in LocationRepository.
    public enum KnownSatellites {
        KENOBI,
        SKYWALKER,
        SATO
    }

    @Getter
    //Satellites received separately are stored here.
    private final ArrayList<Satellite> satellites = new ArrayList<>();

    public void add(Satellite newSatellite) {
        validateSatelliteName(newSatellite);
        //if a satellite with the same name already exists, remove it
        satellites.removeIf((Satellite satellite) ->
                normalizeName(satellite.getName()).equals(normalizeName(newSatellite.getName()))
        );
        satellites.add(newSatellite);
    }

    public void clear() {
        satellites.clear();
    }

    private void validateSatelliteName(Satellite satellite) {
        for (KnownSatellites value : KnownSatellites.values()) {
            if (normalizeName(value.name()).equals(normalizeName(satellite.getName()))) {
                return;
            }
        }
        throw new UnknownSatelliteException(String.format("Satellite %s not found", satellite.getName()));
    }

    private static String normalizeName(String name) {
        return name.toUpperCase(Locale.ROOT);
    }
}
