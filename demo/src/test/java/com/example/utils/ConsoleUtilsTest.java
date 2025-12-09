package com.example.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ConsoleUtilsTest {

    @Test
    void testClear_NoException() {
        assertDoesNotThrow(ConsoleUtils::clear);
    }

    @Test
    void testClear_FallbackPrintsLines() {
        // Simular erro para forçar fallback
        String originalOs = System.getProperty("os.name");
        System.setProperty("os.name", "invalid_os_that_causes_error");

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        assertDoesNotThrow(ConsoleUtils::clear);

        String consoleOutput = output.toString();

        // O fallback dá print de várias quebras de linha
        assertTrue(consoleOutput.split("\n").length >= 10);

        // Restaurar estado original
        System.setProperty("os.name", originalOs);
        System.setOut(new PrintStream(System.out));
    }
}
