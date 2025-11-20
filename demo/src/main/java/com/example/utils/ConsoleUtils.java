package com.example.utils;

public class ConsoleUtils {

    public static void clear() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                // Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Linux / MacOS
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // fallback
            System.out.println("\n".repeat(50));
        }
    }
}
