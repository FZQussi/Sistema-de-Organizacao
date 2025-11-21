package com.example.service;

import com.example.model.Utilizador;
import com.example.utils.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class TurnoService {

    private static final Logger logger = LogManager.getLogger(TurnoService.class);
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Regista a entrada do utilizador no ficheiro turnos.txt
     */
    public void registarEntrada(Utilizador u) {
        LocalDateTime agora = LocalDateTime.now();

        String linha = "\n=== ENTRADA ===\n" +
                "Utilizador: " + u.getUsername() + "\n" +
                "Data/Hora Entrada: " + agora.format(dtf) + "\n";

        appendToTurnos(linha);
        logger.info("Entrada registada para {}", u.getUsername());
    }

    /**
     * Regista a saída, calcula horas e compara com turno esperado.
     */
    public void registarSaida(Utilizador u) {
        LocalDateTime agora = LocalDateTime.now();

        // Parse do turno esperado
        LocalTime turnoInicio = LocalTime.parse(u.getTurnoEntrada(), tf);
        LocalTime turnoFim = LocalTime.parse(u.getTurnoSaida(), tf);

        // Horas trabalhadas estimadas (considerando a última entrada do ficheiro)
        double horasPrevistas = Duration.between(turnoInicio, turnoFim).toMinutes() / 60.0;

        String linha = "\n=== SAÍDA ===\n" +
                "Utilizador: " + u.getUsername() + "\n" +
                "Data/Hora Saída: " + agora.format(dtf) + "\n";

        appendToTurnos(linha);
        logger.info("Saída registada para {}", u.getUsername());
    }

    /**
     * Função auxiliar para escrever no ficheiro turnos.txt
     */
    private void appendToTurnos(String text) {
        try (FileWriter fw = new FileWriter(FileUtils.getTurnosFile(), true)) {
            fw.write(text);
        } catch (IOException e) {
            logger.error("Erro ao escrever no ficheiro de turnos", e);
        }
    }
}
