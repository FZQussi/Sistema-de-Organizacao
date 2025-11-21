package com.example.utils;

import java.time.Duration;
import java.time.LocalTime;

public class CalculadoraTempo {

    // diferença em minutos (real - previsto)
    public static long minutosDiferenca(LocalTime previsto, LocalTime real) {
        return Duration.between(previsto, real).toMinutes();
    }

    // converte minutos para "Xh Ymin"
    public static String formatarMinutos(long minutos) {
        long h = minutos / 60;
        long m = minutos % 60;
        return h + "h " + m + "min";
    }
}
