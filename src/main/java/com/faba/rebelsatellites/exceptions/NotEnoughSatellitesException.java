package com.faba.rebelsatellites.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Not enough satellites recieved. Minimum is 3.")
public class NotEnoughSatellitesException extends RuntimeException {
}
