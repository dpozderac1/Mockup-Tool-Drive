package com.example.demo.ErrorMessageHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ObjectAlreadyExistsException extends RuntimeException{
    public ObjectAlreadyExistsException() {
        super();
    }
    public ObjectAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
    public ObjectAlreadyExistsException(String message) {
        super(message);
    }
    public ObjectAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
