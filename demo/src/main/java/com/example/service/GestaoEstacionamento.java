package com.example.service;

import com.example.model.Carro;
import com.example.model.Estacionamento;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GestaoEstacionamento {

    private static final Logger logger = LogManager.getLogger(GestaoEstacionamento.class);

    private final Estacionamento estacionamento;

    public GestaoEstacionamento(int capacidade) {
        this.estacionamento = new Estacionamento(capacidade);
        logger.info("Gestão de estacionamento inicializada com capacidade: {}", capacidade);
    }

    public void registrarEntrada(String placa, String marca, String modelo, String cor, int ano) {
        Carro carro = new Carro(placa, marca, modelo, cor, ano);
        estacionamento.entrada(carro);
        logger.info("Entrada registrada: {} {} {} {} {}", placa, marca, modelo, cor, ano);
    }

    public void registrarSaida(String placa) {
        estacionamento.saida(placa);
        logger.info("Saída registrada para o carro com placa: {}", placa);
    }

    public void listarVagas() {
        estacionamento.listarVagas();
        logger.info("Listagem de vagas efetuada.");
    }
}
