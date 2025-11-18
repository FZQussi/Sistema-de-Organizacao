package com.example.service;

import com.example.model.Carro;
import com.example.model.Estacionamento;

public class GestaoEstacionamento {

    private Estacionamento estacionamento;

    public GestaoEstacionamento(int capacidade) {
        this.estacionamento = new Estacionamento(capacidade);
    }

    public void registrarEntrada(String placa, String marca, String modelo, String cor, int ano) {
        estacionamento.entrada(new Carro(placa, marca, modelo, cor, ano));
    }

    public void registrarSaida(String placa) {
        estacionamento.saida(placa);
    }

    public void listarVagas() {
        estacionamento.listarVagas();
    }
}
