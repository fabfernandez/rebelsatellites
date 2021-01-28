package com.faba.rebelsatellites.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotEnoughWordsToDecipherMessageException extends RuntimeException {
    public NotEnoughWordsToDecipherMessageException(String message) {
        super(message);
    }
}
