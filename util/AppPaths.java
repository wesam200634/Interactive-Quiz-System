package com.quizapp.util;

import java.nio.file.Path;

/**
 * Central application paths.
 */
public final class AppPaths {
    public static final Path DATA_DIR = Path.of("data");
    public static final Path RESULTS_DIR = Path.of("results");
    public static final Path QUESTIONS_FILE = DATA_DIR.resolve("questions.json");
    public static final Path USERS_FILE = DATA_DIR.resolve("users.json");

    private AppPaths() {
    }
}
