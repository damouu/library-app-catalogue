package com.example.demo.handler;

import com.example.demo.exception.ChapterAlreadyRegisteredException;
import com.example.demo.exception.ChapterNotFoundException;
import com.example.demo.exception.SeriesAlreadyRegisteredException;
import com.example.demo.exception.SeriesNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ChapterNotFoundException.class)
    public ResponseEntity<String> handleException(ChapterNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ChapterAlreadyRegisteredException.class)
    public ResponseEntity<String> handleException(ChapterAlreadyRegisteredException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(SeriesAlreadyRegisteredException.class)
    public ResponseEntity<String> handleException(SeriesAlreadyRegisteredException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(SeriesNotFoundException.class)
    public ResponseEntity<String> handleException(SeriesNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
