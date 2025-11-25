package com.example.model;

import java.time.LocalDateTime;

public class MovimentoEstacionamento {

    private String placa;
    private LocalDateTime entrada;
    private LocalDateTime saida;
    private double valorPago;

    public MovimentoEstacionamento(String placa, LocalDateTime entrada, LocalDateTime saida, double valorPago) {
        this.placa = placa;
        this.entrada = entrada;
        this.saida = saida;
        this.valorPago = valorPago;
    }

    public String getPlaca() { return placa; }
    public LocalDateTime getEntrada() { return entrada; }
    public LocalDateTime getSaida() { return saida; }
    public double getValorPago() { return valorPago; }

    @Override
    public String toString() {
        return placa + " | Entrada: " + entrada + " | Saida: " + saida + " | Pago: " + valorPago + "€";
    }
}
