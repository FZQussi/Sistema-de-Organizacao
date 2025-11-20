package com.example.app.menu.estacionamento;

import com.example.service.GestaoEstacionamento;
import com.example.utils.ConsoleUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class MenuRegistarSaida {

    private static final Logger logger = LogManager.getLogger(MenuRegistarSaida.class);

    private final GestaoEstacionamento gestao;
    private final Scanner sc = new Scanner(System.in);

    public MenuRegistarSaida(GestaoEstacionamento gestao) {
        this.gestao = gestao;
    }

    public void mostrar() {
        while (true) {
            ConsoleUtils.clear();
            System.out.println("===== REGISTAR SAÍDA DE CARRO =====");

            System.out.print("Placa (0 para voltar): ");
            String placa = sc.nextLine();
            if (placa.equals("0")) return;

            boolean sucesso = gestao.registrarSaida(placa);

            if (sucesso) {
                System.out.println("\nSaída registada com sucesso!");
                logger.info("Saída registada para a placa: {}", placa);
            } else {
                System.out.println("\nFalha: Placa não encontrada no estacionamento!");
                logger.warn("Falha ao registar saída para a placa: {}", placa);
            }

            System.out.println("\n1 - Registar outra saída");
            System.out.println("0 - Voltar");
            System.out.print("Escolha: ");

            if (sc.nextLine().equals("0")) return;
        }
    }
}
