package com.example.app.menu.estacionamento;

import com.example.service.GestaoEstacionamento;
import com.example.utils.ConsoleUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class MenuListarCarros {

    private static final Logger logger = LogManager.getLogger(MenuListarCarros.class);

    private final GestaoEstacionamento gestao;
    private final Scanner sc = new Scanner(System.in);

    public MenuListarCarros(GestaoEstacionamento gestao) {
        this.gestao = gestao;
    }

    public void mostrar() {
        while (true) {
            ConsoleUtils.clear();
            System.out.println("===== LISTAGEM DE CARROS NO ESTACIONAMENTO =====");
            gestao.listarVagas();  // <- método do teu serviço

            System.out.println("\n0 - Voltar");
            System.out.print("Escolha: ");

            String opcao = sc.nextLine();

            if (opcao.equals("0")) {
                logger.info("Voltando ao menu principal a partir do MenuListarCarros.");
                return;
            }

            System.out.println("Opcão inválida!");
        }
    }
}
