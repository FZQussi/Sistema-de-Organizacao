package com.example.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Estacionamento {

    private int capacidade;
    private List<Carro> carros;

    public Estacionamento(int capacidade) {
        this.capacidade = capacidade;
        this.carros = new ArrayList<>();
    }

    public boolean entrada(Carro carro) {
        if (carros.size() >= capacidade) {
            System.out.println("⚠ O estacionamento está cheio!");
            return false;
        }

        carros.add(carro);
        System.out.println("✔ Carro " + carro.getPlaca() + " entrou às " + carro.getEntrada());
        return true;
    }

    public boolean saida(String placa) {
        for (Carro c : carros) {
            if (c.getPlaca().equalsIgnoreCase(placa)) {
                c.setSaida(LocalDateTime.now());
                System.out.println("✔ Carro saiu às " + c.getSaida());
                carros.remove(c);
                return true;
            }
        }

        System.out.println("❌ Carro não encontrado!");
        return false;
    }

    public void listarVagas() {
        System.out.println("\n=== Carros no estacionamento ===");
        if (carros.isEmpty()) {
            System.out.println("Nenhum carro estacionado.");
            return;
        }

        carros.forEach(System.out::println);
    }
}
