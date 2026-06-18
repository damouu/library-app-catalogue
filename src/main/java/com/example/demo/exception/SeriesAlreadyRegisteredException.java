package com.example.demo.exception;

import com.example.demo.dto.CreateSeriesRequest;

public class SeriesAlreadyRegisteredException extends RuntimeException {
    public SeriesAlreadyRegisteredException(CreateSeriesRequest series) {
        super(String.format("Series with title %s already exists", series.getTitle()));
    }
}
