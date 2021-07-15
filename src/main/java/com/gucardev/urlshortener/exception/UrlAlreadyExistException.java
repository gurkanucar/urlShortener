package com.gucardev.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UrlAlreadyExistException extends RuntimeException{
    public UrlAlreadyExistException(String message){
        super(message);
    }
}
