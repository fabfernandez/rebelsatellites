package com.faba.rebelsatellites.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UnknownSatelliteException extends RuntimeException {
    public UnknownSatelliteException(String message) {
        super(message);
    }
}
