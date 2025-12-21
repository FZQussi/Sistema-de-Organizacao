package com.example.service;

import com.example.model.Carro;
import com.example.model.Estacionamento;
import com.example.utils.MovimentosUtils;

import java.time.LocalDateTime;
import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Serviço responsável pela gestão do estacionamento.
 * Controla entradas, saídas, cálculo de preços
 * e registo de movimentos em ficheiro.
 */
public class GestaoEstacionamento {

    // Logger para registo de eventos da gestão do estacionamento
    private static final Logger logger = LogManager.getLogger(GestaoEstacionamento.class);

    // Instância do estacionamento gerido por este serviço
    private final Estacionamento estacionamento;

    /**
     * Construtor da gestão de estacionamento.
     *
     * @param capacidade capacidade máxima do estacionamento
     */
    public GestaoEstacionamento(int capacidade) {
        this.estacionamento = new Estacionamento(capacidade);
        logger.info("Gestão de estacionamento inicializada com capacidade: {}", capacidade);
    }

    /**
     * Regista a entrada de um veículo no estacionamento.
     * Cria o objeto Carro e grava o movimento de entrada.
     *
     * @return true se a entrada for bem-sucedida, false caso contrário
     */
    public boolean registrarEntrada(String placa, String marca, String modelo, String cor, int ano) {

        // Cria o carro com os dados fornecidos
        Carro carro = new Carro(placa, marca, modelo, cor, ano);

        // Tenta registar a entrada no estacionamento
        boolean sucesso = estacionamento.entrada(carro);

        // Se a entrada for bem-sucedida, regista o movimento
        if (sucesso) {
            MovimentosUtils.registarEntrada(placa, carro.getEntrada());
            logger.info("Entrada registada para o carro '{}'.", placa);
        }

        return sucesso;
    }

    /**
     * Calcula o preço do estacionamento com base
     * no tempo total em minutos.
     *
     * @param minutos duração do estacionamento em minutos
     * @return valor a pagar
     */
    private double calcularPreco(long minutos) {
        double precoHora = 1.50; // Preço por hora
        return (minutos / 60.0) * precoHora;
    }

    /**
     * Regista a saída de um veículo do estacionamento.
     * Calcula o tempo de permanência, o valor a pagar
     * e grava o movimento de saída.
     *
     * @param placa placa do veículo
     * @return true se a saída for bem-sucedida, false caso contrário
     */
    public boolean registrarSaida(String placa) {

        // Procura o carro no estacionamento
        Carro carro = estacionamento.getCarro(placa);
        if (carro == null) {
            logger.warn("Saída falhou, carro não encontrado: {}", placa);
            return false;
        }

        // Regista a data e hora de saída
        carro.setSaida(LocalDateTime.now());

        // Remove o carro do estacionamento
        estacionamento.saida(placa);

        // Calcula o tempo total estacionado
        long minutos = Duration
                .between(carro.getEntrada(), carro.getSaida())
                .toMinutes();

        // Calcula o valor a pagar
        double valor = calcularPreco(minutos);

        // Regista o movimento de saída em ficheiro
        MovimentosUtils.registarSaida(
                placa,
                carro.getEntrada(),
                carro.getSaida(),
                valor
        );

        logger.info("Saída registada para o carro '{}' com valor: {}€.", placa, valor);
        return true;
    }

    /**
     * Lista os carros atualmente estacionados.
     */
    public void listarVagas() {
        estacionamento.listarVagas();
        logger.info("Listagem de vagas efetuada.");
    }
}
