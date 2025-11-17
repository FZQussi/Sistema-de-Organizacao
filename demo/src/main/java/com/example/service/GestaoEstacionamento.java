package com.example.service;

import com.example.model.Carro;
import com.example.model.Estacionamento;

public class GestaoEstacionamento {
    private Estacionamento estacionamento;

    public GestaoEstacionamento(int capacidade) {
        this.estacionamento = new Estacionamento(capacidade);
    }

    public void registrarEntrada(String placa) {
        estacionamento.entrada(new Carro(placa));
    }

    public void registrarSaida(String placa) {
        estacionamento.saida(placa);
    }

    public void listarVagas() {
        estacionamento.listarVagas();
    }
}