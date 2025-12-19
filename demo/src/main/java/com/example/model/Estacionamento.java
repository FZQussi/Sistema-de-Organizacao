package com.example.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um estacionamento.
 * Controla a capacidade máxima e a lista de carros estacionados.
 */
public class Estacionamento {

    // Capacidade máxima do estacionamento
    private int capacidade;

    // Lista de carros atualmente estacionados
    private List<Carro> carros;

    /**
     * Construtor do estacionamento.
     *
     * @param capacidade número máximo de carros permitidos
     */
    public Estacionamento(int capacidade) {
        this.capacidade = capacidade;
        this.carros = new ArrayList<>();
    }

    /**
     * Procura um carro no estacionamento pela placa.
     *
     * @param placa placa do veículo a procurar
     * @return objeto Carro se encontrado, ou null caso contrário
     */
    public Carro getCarro(String placa) {
        for (Carro c : carros) {
            if (c.getPlaca().equalsIgnoreCase(placa)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Regista a entrada de um carro no estacionamento.
     *
     * @param carro carro a ser estacionado
     * @return true se a entrada for bem-sucedida, false se estiver cheio
     */
    public boolean entrada(Carro carro) {

        // Verifica se o estacionamento atingiu a capacidade máxima
        if (carros.size() >= capacidade) {
            System.out.println("⚠ O estacionamento está cheio!");
            return false;
        }

        // Adiciona o carro à lista
        carros.add(carro);

        // Confirma a entrada com data e hora
        System.out.println("✔ Carro " + carro.getPlaca() + " entrou às " + carro.getEntrada());
        return true;
    }

    /**
     * Regista a saída de um carro com base na placa.
     *
     * @param placa placa do veículo que vai sair
     * @return true se o carro for encontrado e removido, false caso contrário
     */
    public boolean saida(String placa) {

        // Percorre a lista de carros estacionados
        for (Carro c : carros) {
            if (c.getPlaca().equalsIgnoreCase(placa)) {

                // Regista a data e hora de saída
                c.setSaida(LocalDateTime.now());

                System.out.println("✔ Carro saiu às " + c.getSaida());

                // Remove o carro do estacionamento
                carros.remove(c);
                return true;
            }
        }

        // Caso o carro não seja encontrado
        System.out.println("❌ Carro não encontrado!");
        return false;
    }

    /**
     * Lista todos os carros atualmente estacionados.
     */
    public void listarVagas() {
        System.out.println("\n=== Carros no estacionamento ===");

        // Verifica se não há carros estacionados
        if (carros.isEmpty()) {
            System.out.println("Nenhum carro estacionado.");
            return;
        }

        // Imprime cada carro utilizando o método toString()
        carros.forEach(System.out::println);
    }
}
