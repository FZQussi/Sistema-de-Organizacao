package com.example.model;

import java.time.LocalDateTime;

public class Carro {
    private String placa;
    private String marca;
    private String modelo;
    private String cor;
    private int ano;

    private LocalDateTime entrada;
    private LocalDateTime saida;

    public Carro(String placa, String marca, String modelo, String cor, int ano) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.cor = cor;
        this.ano = ano;
        this.entrada = LocalDateTime.now();
    }

    public String getPlaca() { return placa; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public String getCor() { return cor; }
    public int getAno() { return ano; }

    public LocalDateTime getEntrada() { return entrada; }
    public LocalDateTime getSaida() { return saida; }
    public void setSaida(LocalDateTime saida) { this.saida = saida; }

    @Override
    public String toString() {
        return placa + " | " + marca + " " + modelo + " (" + ano + ") - " + cor +
                " | Entrada: " + entrada +
                (saida != null ? " | Saída: " + saida : "");
    }
}
