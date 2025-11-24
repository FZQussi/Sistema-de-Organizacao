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

    // Cores ANSI
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BOLD = "\u001B[1m";

    public MenuListarCarros(GestaoEstacionamento gestao) {
        this.gestao = gestao;
    }

    private void header() {
        System.out.println(CYAN + BOLD + "╔═══════════════════════════════════════════════════╗");
        System.out.println("║         LISTAGEM DE CARROS NO ESTACIONAMENTO      ║");
        System.out.println("╚═══════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    public void mostrar() {
        while (true) {
            ConsoleUtils.clear();
            header();

            System.out.println(GREEN + BOLD + "Carros atualmente estacionados:\n" + RESET);

            gestao.listarVagas(); // <- método do teu serviço (assume que imprime cada vaga)

            System.out.println(CYAN + "\n────────────────────────────────────────────────────" + RESET);
            System.out.println(YELLOW + "0 - Voltar" + RESET);
            System.out.print(YELLOW + "→ Escolha: " + RESET);

            String opcao = sc.nextLine().trim();

            if (opcao.equals("0")) {
                logger.info("Voltando ao menu principal a partir do MenuListarCarros.");
                return;
            }

            System.out.println(RED + BOLD + "\n✖ Opção inválida! Tente novamente." + RESET);
            System.out.println(CYAN + "────────────────────────────────────────────────────\n" + RESET);
        }
    }
}

