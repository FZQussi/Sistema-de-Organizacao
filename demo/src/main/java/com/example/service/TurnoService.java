package com.example.service;

import com.example.model.Utilizador;
import com.example.utils.CalculadoraTempo;
import com.example.utils.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.time.*;

/**
 * Serviço responsável pela gestão de turnos dos utilizadores.
 * Regista entradas, saídas, atrasos e tempo total trabalhado,
 * persistindo a informação em ficheiro.
 */
public class TurnoService {

    // Logger para registo e auditoria de operações de turnos
    private static final Logger logger = LogManager.getLogger(TurnoService.class);

    // Utilizador atualmente em turno
    private Utilizador userEmTurno;

    // Data e hora reais da entrada no turno
    private LocalDateTime entradaReal;

    /**
     * Regista a entrada de turno de um utilizador.
     * Calcula o atraso relativamente ao horário previsto
     * e grava a informação no ficheiro de turnos.
     *
     * @param u utilizador que inicia o turno
     */
    public void registarEntrada(Utilizador u) {

        // Define o utilizador em turno e o momento real da entrada
        this.userEmTurno = u;
        this.entradaReal = LocalDateTime.now();

        // Horário de entrada previsto definido no utilizador
        String entradaPrevistaStr = u.getTurnoEntrada();
        LocalTime entradaPrevista = LocalTime.parse(entradaPrevistaStr);

        // Hora real de entrada
        LocalTime entradaRealTime = entradaReal.toLocalTime();

        // Calcula atraso em minutos
        long atrasoMin = CalculadoraTempo.minutosDiferenca(
                entradaPrevista, entradaRealTime);

        // Grava os dados da entrada no ficheiro
        gravarNoFicheiro(
                "\n[" + LocalDate.now() + "] " + u.getUsername() + " (" + u.getTipo() + ")\n" +
                " - Entrada prevista: " + entradaPrevista + "\n" +
                " - Entrada real:     " + entradaRealTime + "\n" +
                " - Atraso:           " + atrasoMin + " minutos\n"
        );

        logger.info(
                "Entrada de turno registada para '{}'. Atraso: {} minutos",
                u.getUsername(), atrasoMin
        );
    }

    /**
     * Regista a saída de turno de um utilizador.
     * Calcula diferenças relativamente ao horário previsto
     * e o total de minutos trabalhados.
     *
     * @param u utilizador que termina o turno
     */
    public void registarSaida(Utilizador u) {

        // Se não houver utilizador em turno, não faz nada
        if (userEmTurno == null) return;

        // Momento real da saída
        LocalDateTime saidaReal = LocalDateTime.now();

        // Horário de saída previsto
        LocalTime saidaPrevista = LocalTime.parse(u.getTurnoSaida());

        // Hora real de saída
        LocalTime saidaRealTime = saidaReal.toLocalTime();

        // Diferença entre saída prevista e real (adiantamento ou atraso)
        long diferencaSaida = CalculadoraTempo.minutosDiferenca(
                saidaPrevista, saidaRealTime);

        // Total de minutos trabalhados no turno
        long minutosTrabalhados =
                Duration.between(entradaReal, saidaReal).toMinutes();

        // Grava os dados da saída no ficheiro
        gravarNoFicheiro(
                " - Saída prevista:   " + saidaPrevista + "\n" +
                " - Saída real:       " + saidaRealTime + "\n" +
                " - Diferença saída:  " + diferencaSaida + " minutos\n" +
                " - Total trabalhado: " +
                CalculadoraTempo.formatarMinutos(minutosTrabalhados) + "\n" +
                "-----------------------------------------\n"
        );

        logger.info(
                "Saída de turno registada para '{}'. Diferença saída: {} minutos",
                u.getUsername(), diferencaSaida
        );

        // Limpa o utilizador em turno
        this.userEmTurno = null;
    }

    /**
     * Escreve texto no ficheiro de turnos em modo append.
     *
     * @param texto conteúdo a gravar
     */
    private void gravarNoFicheiro(String texto) {
        try (FileWriter fw = new FileWriter(FileUtils.getTurnosFile(), true)) {
            fw.write(texto);
        } catch (IOException e) {
            logger.error("Erro ao escrever no ficheiro de turnos.", e);
        }
    }
}
