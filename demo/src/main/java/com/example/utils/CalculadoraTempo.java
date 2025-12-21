package com.example.utils;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Classe utilitária para cálculos relacionados com tempo.
 * Fornece métodos estáticos para cálculo de diferenças
 * e formatação de minutos.
 */
public class CalculadoraTempo {

    /**
     * Calcula a diferença em minutos entre o horário previsto
     * e o horário real.
     *
     * Resultado:
     *  - Positivo  → atraso
     *  - Negativo  → adiantamento
     *
     * @param previsto horário previsto
     * @param real     horário real
     * @return diferença em minutos (real - previsto)
     */
    public static long minutosDiferenca(LocalTime previsto, LocalTime real) {
        return Duration
                .between(previsto, real)
                .toMinutes();
    }

    /**
     * Converte um valor em minutos para o formato legível:
     * "Xh Ymin".
     *
     * @param minutos total de minutos
     * @return string formatada em horas e minutos
     */
    public static String formatarMinutos(long minutos) {
        long h = minutos / 60;
        long m = minutos % 60;
        return h + "h " + m + "min";
    }
}
