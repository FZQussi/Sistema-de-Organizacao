package com.example.model;

import java.time.LocalDateTime;

/**
 * Classe que representa um carro no sistema.
 * Armazena informações de identificação do veículo
 * e os registos de entrada e saída.
 */
public class Carro {

    // Identificação do veículo
    private String placa;
    private String marca;
    private String modelo;
    private String cor;
    private int ano;

    // Data e hora de entrada e saída do veículo
    private LocalDateTime entrada;
    private LocalDateTime saida;

    /**
     * Construtor da classe Carro.
     * Inicializa os dados do veículo e regista automaticamente
     * a data e hora de entrada no sistema.
     *
     * @param placa  placa do veículo
     * @param marca  marca do veículo
     * @param modelo modelo do veículo
     * @param cor    cor do veículo
     * @param ano    ano de fabrico
     */
    public Carro(String placa, String marca, String modelo, String cor, int ano) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.cor = cor;
        this.ano = ano;

        // Regista o momento de entrada automaticamente
        this.entrada = LocalDateTime.now();
    }

    // Métodos getters para acesso aos dados do veículo
    public String getPlaca() { return placa; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public String getCor() { return cor; }
    public int getAno() { return ano; }

    // Retorna a data/hora de entrada
    public LocalDateTime getEntrada() { return entrada; }

    // Retorna a data/hora de saída
    public LocalDateTime getSaida() { return saida; }

    /**
     * Define manualmente a data/hora de entrada.
     * Útil em casos de carregamento de dados ou correções.
     */
    public void setEntrada(LocalDateTime entrada) {
        this.entrada = entrada;
    }

    /**
     * Define a data/hora de saída do veículo.
     */
    public void setSaida(LocalDateTime saida) {
        this.saida = saida;
    }

    /**
     * Retorna uma representação textual do objeto Carro.
     * A saída só é exibida se existir (não for null).
     */
    @Override
    public String toString() {
        return placa + " | " + marca + " " + modelo + " (" + ano + ") - " + cor +
                " | Entrada: " + entrada +
                (saida != null ? " | Saída: " + saida : "");
    }
}
