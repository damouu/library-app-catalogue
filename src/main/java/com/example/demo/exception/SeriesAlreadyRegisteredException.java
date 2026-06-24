package com.example.demo.exception;

public class SeriesAlreadyRegisteredException extends RuntimeException {

    private final String title;

    public SeriesAlreadyRegisteredException(String title) {
        super("Series already exists: " + title);
        this.title = title;
    }
}
