package com.example.model;
import java.time.Duration;
import java.time.LocalDateTime;

public class Carro {
    private String placa;
    private LocalDateTime horaEntrada;
    private LocalDateTime horaSaida;

    public Carro(String placa) {
        this.placa = placa;
        this.horaEntrada = LocalDateTime.now();
    }

    public String getPlaca() {
        return placa;
    }

    public LocalDateTime getHoraEntrada() {
        return horaEntrada;
    }

    public LocalDateTime getHoraSaida() {
        return horaSaida;
    }

    public void setHoraSaida(LocalDateTime horaSaida) {
        this.horaSaida = horaSaida;
    }

    public double calcularValor() {
        if (horaSaida == null) return 0;

        Duration tempo = Duration.between(horaEntrada, horaSaida);
        long minutos = tempo.toMinutes();

        if (minutos <= 60) return 0; // 1h gratuita

        double horasExtras = Math.ceil((minutos - 60) / 60.0);
        double valorPorHora = 5.0; // valor por hora extra
        return horasExtras * valorPorHora;
    }
}