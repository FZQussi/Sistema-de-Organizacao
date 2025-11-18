package com.example.model;

import java.time.LocalDateTime;

public class Operador extends Utilizador {

    private LocalDateTime inicioTurno;
    private LocalDateTime fimTurno;

    public void iniciarTurno() {
        inicioTurno = LocalDateTime.now();
    }

    public void terminarTurno() {
        fimTurno = LocalDateTime.now();
    }

    public LocalDateTime getInicioTurno() { return inicioTurno; }
    public LocalDateTime getFimTurno() { return fimTurno; }
}
