package com.example.model;

import java.time.LocalDateTime;

/**
 * Classe que representa um operador do sistema.
 * Herda os dados base de um Utilizador e adiciona
 * o controlo de turnos de trabalho.
 */
public class Operador extends Utilizador {

    // Data e hora de início do turno do operador
    private LocalDateTime inicioTurno;

    // Data e hora de fim do turno do operador
    private LocalDateTime fimTurno;

    /**
     * Regista o início do turno do operador,
     * guardando a data e hora atuais.
     */
    public void iniciarTurno() {
        inicioTurno = LocalDateTime.now();
    }

    /**
     * Regista o fim do turno do operador,
     * guardando a data e hora atuais.
     */
    public void terminarTurno() {
        fimTurno = LocalDateTime.now();
    }

    // Retorna a data e hora de início do turno
    public LocalDateTime getInicioTurno() {
        return inicioTurno;
    }

    // Retorna a data e hora de fim do turno
    public LocalDateTime getFimTurno() {
        return fimTurno;
    }
}
