package com.havenhr.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ConstraintsViolationException extends Exception {

    public ConstraintsViolationException(String message) {
        super(message);
    }

}
