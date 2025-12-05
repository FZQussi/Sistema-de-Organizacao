package com.example.utils;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

public class CalculadoraTempoTest {

    @Test
    void testMinutosDiferenca_Positivo() {
        LocalTime previsto = LocalTime.of(9, 0);
        LocalTime real = LocalTime.of(10, 15);

        long minutos = CalculadoraTempo.minutosDiferenca(previsto, real);

        assertEquals(75, minutos); // 1h15 → 75 min
    }

    @Test
    void testMinutosDiferenca_Negativo() {
        LocalTime previsto = LocalTime.of(10, 30);
        LocalTime real = LocalTime.of(9, 45);

        long minutos = CalculadoraTempo.minutosDiferenca(previsto, real);

        assertEquals(-45, minutos); // real antes → negativo
    }

    @Test
    void testMinutosDiferenca_Zero() {
        LocalTime previsto = LocalTime.of(14, 0);
        LocalTime real = LocalTime.of(14, 0);

        long minutos = CalculadoraTempo.minutosDiferenca(previsto, real);

        assertEquals(0, minutos);
    }

    @Test
    void testFormatarMinutos_Positivo() {
        String resultado = CalculadoraTempo.formatarMinutos(125);

        assertEquals("2h 5min", resultado);
    }

    @Test
    void testFormatarMinutos_Negativo() {
        String resultado = CalculadoraTempo.formatarMinutos(-90);

        assertEquals("-2h -30min", resultado);
    }

    @Test
    void testFormatarMinutos_Zero() {
        String resultado = CalculadoraTempo.formatarMinutos(0);

        assertEquals("0h 0min", resultado);
    }
}
