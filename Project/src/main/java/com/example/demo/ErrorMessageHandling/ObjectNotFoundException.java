package com.example.demo.ErrorMessageHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException() {
        super();
    }
    public ObjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public ObjectNotFoundException(String message) {
        super(message);
    }
    public ObjectNotFoundException(Throwable cause) {
        super(cause);
    }
}
