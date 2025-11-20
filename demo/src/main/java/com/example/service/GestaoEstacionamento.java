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

    public boolean registrarEntrada(String placa, String marca, String modelo, String cor, int ano) {
    Carro carro = new Carro(placa, marca, modelo, cor, ano);
    boolean sucesso = estacionamento.entrada(carro); // entrada retorna true/false
    if (sucesso) {
        logger.info("Entrada registrada: {} {} {} {} {}", placa, marca, modelo, cor, ano);
    } else {
        logger.warn("Falha ao registrar entrada: estacionamento cheio ou carro já existe: {}", placa);
    }
    return sucesso;
}

public boolean registrarSaida(String placa) {
    boolean sucesso = estacionamento.saida(placa); // saída retorna true/false
    if (sucesso) {
        logger.info("Saída registrada para o carro com placa: {}", placa);
    } else {
        logger.warn("Falha ao registrar saída: carro não encontrado: {}", placa);
    }
    return sucesso;
}


    public void listarVagas() {
        estacionamento.listarVagas();
        logger.info("Listagem de vagas efetuada.");
    }
}
