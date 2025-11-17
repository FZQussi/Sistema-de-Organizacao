package com.example.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Estacionamento {
    private List<Carro> vagas;
    private int capacidade;

    public Estacionamento(int capacidade) {
        this.capacidade = capacidade;
        this.vagas = new ArrayList<>();
    }

    public boolean entrada(Carro carro) {
        if (vagas.size() >= capacidade) {
            System.out.println("Estacionamento cheio!");
            return false;
        }
        vagas.add(carro);
        System.out.println("Carro " + carro.getPlaca() + " entrou às " + carro.getHoraEntrada());
        return true;
    }

    public void saida(String placa) {
        Carro carroEncontrado = null;
        for (Carro c : vagas) {
            if (c.getPlaca().equalsIgnoreCase(placa)) {
                carroEncontrado = c;
                break;
            }
        }

        if (carroEncontrado != null) {
            carroEncontrado.setHoraSaida(LocalDateTime.now());
            double valor = carroEncontrado.calcularValor();
            System.out.println("Carro " + placa + " saiu às " + carroEncontrado.getHoraSaida()
                    + ", valor a pagar: R$" + valor);
            vagas.remove(carroEncontrado);
        } else {
            System.out.println("Carro não encontrado!");
        }
    }

    public void listarVagas() {
        System.out.println("Vagas ocupadas: " + vagas.size() + "/" + capacidade);
        for (Carro c : vagas) {
            System.out.println("- " + c.getPlaca() + " entrou às " + c.getHoraEntrada());
        }
    }
}