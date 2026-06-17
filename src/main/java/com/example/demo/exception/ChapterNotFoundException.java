package com.example.demo.exception;

import java.util.UUID;

public class ChapterNotFoundException extends RuntimeException {
    public ChapterNotFoundException(UUID chapterUUID) {
        super("Could not find chapter with uuid " + chapterUUID);
    }
}
