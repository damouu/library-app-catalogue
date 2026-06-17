package com.example.demo.exception;

import java.util.UUID;

public class SeriesNotFoundException extends RuntimeException {

    public SeriesNotFoundException(UUID seriesUUID) {
        super("Could not find series with UUID" + seriesUUID);
    }
}
