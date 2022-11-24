package com.myproject.tinkoffservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {
    public ResponseEntity<ErrorDto> handleNotFound(Exception e) {
        return new ResponseEntity<ErrorDto>(new ErrorDto(e.getLocalizedMessage()), HttpStatus.NOT_FOUND);
    }
}
