package dev.vortsu.controllers;

import dev.vortsu.utils.exceptions.CustomException;
import dev.vortsu.utils.exceptions.StudentNotFoundException;
import dev.vortsu.utils.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException ex){
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(value = StudentNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(StudentNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }
}
