package com.example.service;

import com.example.model.Utilizador;
import com.example.utils.CalculadoraTempo;
import com.example.utils.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class TurnoService {

    private static final Logger logger = LogManager.getLogger(TurnoService.class);

    private Utilizador userEmTurno;
    private LocalDateTime entradaReal;

    public void registarEntrada(Utilizador u) {
        this.userEmTurno = u;
        this.entradaReal = LocalDateTime.now();

        String entradaPrevistaStr = u.getTurnoEntrada();
        LocalTime entradaPrevista = LocalTime.parse(entradaPrevistaStr);
        LocalTime entradaRealTime = entradaReal.toLocalTime();

        long atrasoMin = CalculadoraTempo.minutosDiferenca(entradaPrevista, entradaRealTime);

        gravarNoFicheiro(
                "\n[" + LocalDate.now() + "] " + u.getUsername() + " (" + u.getTipo() + ")\n" +
                " - Entrada prevista: " + entradaPrevista + "\n" +
                " - Entrada real:     " + entradaRealTime + "\n" +
                " - Atraso:           " + atrasoMin + " minutos\n"
        );

        logger.info("Entrada de turno registada para '{}'. Atraso: {} minutos", u.getUsername(), atrasoMin);
    }

    public void registarSaida(Utilizador u) {
        if (userEmTurno == null) return;

        LocalDateTime saidaReal = LocalDateTime.now();

        LocalTime saidaPrevista = LocalTime.parse(u.getTurnoSaida());
        LocalTime saidaRealTime = saidaReal.toLocalTime();

        long diferencaSaida = CalculadoraTempo.minutosDiferenca(saidaPrevista, saidaRealTime);

        long minutosTrabalhados =
                Duration.between(entradaReal, saidaReal).toMinutes();

        gravarNoFicheiro(
                " - Saída prevista:   " + saidaPrevista + "\n" +
                " - Saída real:       " + saidaRealTime + "\n" +
                " - Diferença saída:  " + diferencaSaida + " minutos\n" +
                " - Total trabalhado: " + CalculadoraTempo.formatarMinutos(minutosTrabalhados) + "\n" +
                "-----------------------------------------\n"
        );

        logger.info("Saída de turno registada para '{}'. Diferença saída: {} minutos", u.getUsername(), diferencaSaida);

        this.userEmTurno = null;
    }

    private void gravarNoFicheiro(String texto) {
        try (FileWriter fw = new FileWriter(FileUtils.getTurnosFile(), true)) {
            fw.write(texto);
        } catch (IOException e) {
            logger.error("Erro ao escrever no ficheiro de turnos.", e);
        }
    }
}
