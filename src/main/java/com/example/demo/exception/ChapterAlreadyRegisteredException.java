package com.example.demo.exception;

public class ChapterAlreadyRegisteredException extends RuntimeException {

    public ChapterAlreadyRegisteredException(Integer chapterNumber) {
        super("この巻は既に登録されています " + chapterNumber);
    }
}
