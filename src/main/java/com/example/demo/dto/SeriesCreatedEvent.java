package com.example.demo.dto;

public record SeriesCreatedEvent(
        Metadata metadata,
        SeriesCreatedEventData data
) {
}