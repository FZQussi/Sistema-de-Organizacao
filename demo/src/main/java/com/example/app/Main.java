package com.example.app;

import com.example.service.GestaoEstacionamento;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GestaoEstacionamento gestao = new GestaoEstacionamento(5);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- MENU ESTACIONAMENTO ---");
            System.out.println("1. Entrada de carro");
            System.out.println("2. Saída de carro");
            System.out.println("3. Listar vagas");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // limpar buffer

            switch (opcao) {
                case 1:
                    System.out.print("Digite a placa: ");
                    String placaEntrada = scanner.nextLine();
                    gestao.registrarEntrada(placaEntrada);
                    break;
                case 2:
                    System.out.print("Digite a placa: ");
                    String placaSaida = scanner.nextLine();
                    gestao.registrarSaida(placaSaida);
                    break;
                case 3:
                    gestao.listarVagas();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    System.exit(0);
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
}
