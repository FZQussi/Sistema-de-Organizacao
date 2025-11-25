package com.example.service;

import com.example.model.Carro;
import com.example.model.Estacionamento;
import com.example.utils.MovimentosUtils;

import java.time.LocalDateTime;

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

    boolean sucesso = estacionamento.entrada(carro);
    if (sucesso) {
        MovimentosUtils.registarEntrada(placa, carro.getEntrada());
    }

    return sucesso;
}

}
private double calcularPreco(long minutos) {
    double precoHora = 1.50;  // por exemplo
    return (minutos / 60.0) * precoHora;
}

public boolean registrarSaida(String placa) {

    Carro carro = estacionamento.getCarro(placa);
    if (carro == null) {
        logger.warn("Saída falhou, carro não encontrado: {}", placa);
        return false;
    }

    carro.setSaida(LocalDateTime.now());
    estacionamento.saida(placa);

    long minutos = Duration.between(carro.getEntrada(), carro.getSaida()).toMinutes();
    double valor = calcularPreco(minutos);

    // gravar no movimentos.txt
    MovimentosUtils.registarSaida(placa, carro.getEntrada(), carro.getSaida(), valor);

    return true;
}




    public void listarVagas() {
        estacionamento.listarVagas();
        logger.info("Listagem de vagas efetuada.");
    }
}
