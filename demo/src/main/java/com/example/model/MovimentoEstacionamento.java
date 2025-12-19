package com.example.model;

import java.time.LocalDateTime;

/**
 * Classe que representa um movimento de estacionamento.
 * Regista as informações de entrada, saída e valor pago
 * associadas a um veículo.
 */
public class MovimentoEstacionamento {

    // Placa do veículo associado ao movimento
    private String placa;

    // Data e hora de entrada no estacionamento
    private LocalDateTime entrada;

    // Data e hora de saída do estacionamento
    private LocalDateTime saida;

    // Valor pago pelo período de estacionamento
    private double valorPago;

    /**
     * Construtor da classe MovimentoEstacionamento.
     *
     * @param placa     placa do veículo
     * @param entrada   data e hora de entrada
     * @param saida     data e hora de saída
     * @param valorPago valor total pago
     */
    public MovimentoEstacionamento(String placa, LocalDateTime entrada, LocalDateTime saida, double valorPago) {
        this.placa = placa;
        this.entrada = entrada;
        this.saida = saida;
        this.valorPago = valorPago;
    }

    // Métodos getters para acesso aos dados do movimento
    public String getPlaca() { return placa; }
    public LocalDateTime getEntrada() { return entrada; }
    public LocalDateTime getSaida() { return saida; }
    public double getValorPago() { return valorPago; }

    /**
     * Retorna uma representação textual do movimento de estacionamento,
     * formatada para apresentação ao utilizador.
     */
    @Override
    public String toString() {
        return placa +
                " | Entrada: " + entrada +
                " | Saída: " + saida +
                " | Pago: " + valorPago + "€";
    }
}
