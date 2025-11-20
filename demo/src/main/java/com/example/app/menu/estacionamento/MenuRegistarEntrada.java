package com.example.app.menu.estacionamento;

import com.example.service.GestaoEstacionamento;
import com.example.utils.ConsoleUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class MenuRegistarEntrada {

    private static final Logger logger = LogManager.getLogger(MenuRegistarEntrada.class);

    private final GestaoEstacionamento gestao;
    private final Scanner sc = new Scanner(System.in);

    public MenuRegistarEntrada(GestaoEstacionamento gestao) {
        this.gestao = gestao;
    }

    public void mostrar() {
        while (true) {
            ConsoleUtils.clear();
            System.out.println("===== REGISTAR ENTRADA DE CARRO =====");

            System.out.print("Placa (0 para voltar): ");
            String placa = sc.nextLine();
            if (placa.equals("0")) return;

            System.out.print("Marca: ");
            String marca = sc.nextLine();

            System.out.print("Modelo: ");
            String modelo = sc.nextLine();

            System.out.print("Cor: ");
            String cor = sc.nextLine();

            int ano;
            while (true) {
                System.out.print("Ano: ");
                try {
                    ano = Integer.parseInt(sc.nextLine());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Ano inválido! Digite apenas números.");
                }
            }

            boolean sucesso = gestao.registrarEntrada(placa, marca, modelo, cor, ano);

            if (sucesso) {
                System.out.println("\nCarro registado com sucesso!");
                logger.info("Entrada registada: {} {} {} {} {}", placa, marca, modelo, cor, ano);
            } else {
                System.out.println("\nFalha: Estacionamento cheio ou placa já registada!");
                logger.warn("Falha ao registar entrada para placa: {}", placa);
            }

            System.out.println("\n1 - Registar outra entrada");
            System.out.println("0 - Voltar");
            System.out.print("Escolha: ");

            if (sc.nextLine().equals("0")) return;
        }
    }
}
