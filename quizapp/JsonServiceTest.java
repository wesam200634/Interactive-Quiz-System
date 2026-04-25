package com.quizapp;

import com.quizapp.service.JsonService;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests simple file storage helpers.
 */
public class JsonServiceTest {
    @Test
    void writeAndReadTextShouldWork() throws Exception {
        JsonService service = new JsonService();
        Path tempDir = Files.createTempDirectory("quizapp-test");
        Path file = tempDir.resolve("sample.txt");
        service.writeText(file, "hello world");
        Assertions.assertEquals("hello world", service.readText(file));
    }
}
