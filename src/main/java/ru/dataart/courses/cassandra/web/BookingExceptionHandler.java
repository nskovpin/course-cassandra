package ru.dataart.courses.cassandra.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestController
@ControllerAdvice
public class BookingExceptionHandler extends ResponseEntityExceptionHandler {


//    @ExceptionHandler
//    public ResponseEntity<?> handleCustomError(){
//        return new ResponseEntity<Object>("", HttpStatus.BAD_REQUEST);
//    }


}
